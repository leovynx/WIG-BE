package com.whereitgo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whereitgo.exceptionHandling.TransactionExceptions;
import com.whereitgo.model.Transaction;
import com.whereitgo.model.TransactionRawSMS;
import com.whereitgo.model.User;
import com.whereitgo.repository.TransactionRawSMSRepo;
import com.whereitgo.repository.TransactionRepo;
import com.whereitgo.utility.enums.TransactionType;
import com.whereitgo.utility.httpEntity.TransactionDTO;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionRawSMSRepo rawSMSRepo;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "dd-MMM-yy HH:mm:ss",
            Locale.ENGLISH);

    public Transaction parse(String sms) {

        if (sms == null || sms.isBlank()) {
            throw new TransactionExceptions.SMSParsingException(
                    "SMS cannot be null or empty");
        }

        try {

            Transaction txn = new Transaction();

            extractAmount(sms, txn);
            extractTransactionType(sms, txn);
            extractDate(sms, txn);
            extractCounterparty(sms, txn);

            txn.setBankName("ICICI");

            return txn;

        } catch (TransactionExceptions.SMSParsingException | TransactionExceptions.InvalidTransactionDateException
                | TransactionExceptions.InvalidTransactionAmountException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new TransactionExceptions.SMSParsingException(
                    "Failed to parse ICICI SMS. Reason: "
                            + ex.getMessage());
        }
    }

    private void extractAmount(String sms, Transaction txn) {

        Matcher matcher = Pattern
                .compile(
                        "Rs\\s*(\\d+(?:\\.\\d+)?)",
                        Pattern.CASE_INSENSITIVE)
                .matcher(sms);

        if (!matcher.find()) {
            throw new TransactionExceptions.SMSParsingException(
                    "Amount not found in ICICI SMS");
        }

        try {
            txn.setAmount(new BigDecimal(matcher.group(1)));

        } catch (NumberFormatException ex) {

            throw new TransactionExceptions.InvalidTransactionAmountException(
                    "Invalid amount: " + matcher.group(1));
        }
    }

    private void extractTransactionType(
            String sms,
            Transaction txn) {

        Matcher matcher = Pattern
                .compile(
                        "\\b(credited|debited)\\b",
                        Pattern.CASE_INSENSITIVE)
                .matcher(sms);

        if (!matcher.find()) {
            throw new TransactionExceptions.SMSParsingException(
                    "Transaction type not found in ICICI SMS");
        }

        String type = matcher.group(1).toUpperCase();

        switch (type) {

            case "DEBITED":
                txn.setTransactionType(TransactionType.DEBIT);
                break;

            case "CREDITED":
                txn.setTransactionType(TransactionType.CREDIT);
                break;

            default:
                throw new TransactionExceptions.SMSParsingException(
                        "Unsupported transaction type: " + type);
        }
    }

    private void extractDate(
            String sms,
            Transaction txn) {

        Matcher matcher = Pattern
                .compile("\\b\\d{1,2}-[A-Za-z]{3}-\\d{2}\\b")
                .matcher(sms);

        if (!matcher.find()) {
            throw new TransactionExceptions.SMSParsingException(
                    "Transaction date not found in ICICI SMS");
        }

        try {

            String date = matcher.group();

            txn.setTransactionTime(
                    LocalDateTime.parse(
                            date + " 00:00:00",
                            DATE_FORMATTER));

        } catch (DateTimeParseException ex) {

            throw new TransactionExceptions.InvalidTransactionDateException(
                    "Invalid transaction date: "
                            + matcher.group());
        }
    }

    private void extractCounterparty(
            String sms,
            Transaction txn) {

        Matcher matcher = Pattern
                .compile(
                        ";\\s+(.*?)\\s+credited\\.",
                        Pattern.CASE_INSENSITIVE)
                .matcher(sms);

        if (!matcher.find()) {
            throw new TransactionExceptions.SMSParsingException(
                    "Counterparty not found in ICICI SMS");
        }

        String counterparty = matcher.group(1).trim();

        if (counterparty.isEmpty()) {
            throw new TransactionExceptions.SMSParsingException(
                    "Counterparty name is empty");
        }

        txn.setCounterpartyName(counterparty);
    }

    private TransactionDTO convertToResponse(Transaction transaction, String rawMessage) {

        TransactionDTO response = new TransactionDTO();

        response.setTransactionId(transaction.getId());
        response.setTransactionType(transaction.getTransactionType());
        response.setTransactionStatus(transaction.getTransactionStatus());
        response.setAmount(transaction.getAmount());
        response.setCurrency(transaction.getCurrency());
        response.setBankName(transaction.getBankName());
        response.setCounterpartyName(transaction.getCounterpartyName());
        response.setTransactionTime(transaction.getTransactionTime());
        response.setIsUserConfirmed(transaction.getIsUserConfirmed());
        response.setRawMessage(rawMessage);

        return response;
    }

    public TransactionDTO processTransactionRawMessage(TransactionDTO rawMessage) {

        User userInfo = userService.getCurrentUser();

        TransactionRawSMS rawSMS = new TransactionRawSMS();
        rawSMS.setSmsText(rawMessage.getRawMessage());
        rawSMS.setUser(userInfo);
        rawSMS = rawSMSRepo.save(rawSMS);

        Transaction transaction = parse(rawMessage.getRawMessage());

        transaction.setUser(userInfo);
        transaction.setRawSms(rawSMS);

        transaction = transactionRepo.save(transaction);

        return convertToResponse(transaction, rawMessage.getRawMessage());
    }

    public String saveTransactionMessage(TransactionDTO message) {

        try {

            User currentUser = userService.getCurrentUser();

            Transaction transaction = transactionRepo.findById(message.getTransactionId())
                    .orElseThrow(() -> new TransactionExceptions.TransactionNotFoundException(
                            "Transaction not found"));

            if (!transaction.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new TransactionExceptions.TransactionAccessDeniedException(
                        "You are not allowed to update this transaction.");
            }

            if (Boolean.TRUE.equals(transaction.getIsUserConfirmed())) {
                throw new TransactionExceptions.TransactionAlreadyConfirmedException(
                        "Transaction has already been confirmed.");
            }

            if (Boolean.TRUE.equals(message.getIsUserConfirmed())
                    && (message.getTransactionNotes() == null
                            || message.getTransactionNotes().isBlank())) {

                throw new TransactionExceptions.InvalidTransactionNotesException(
                        "Transaction notes cannot be empty.");
            }

            transaction.setIsUserConfirmed(Boolean.TRUE.equals(message.getIsUserConfirmed()));
            transaction.setNotes(message.getTransactionNotes());

            transactionRepo.save(transaction);

            return "Transaction updated successfully.";

        } catch (TransactionExceptions.TransactionNotFoundException
                | TransactionExceptions.TransactionAccessDeniedException
                | TransactionExceptions.TransactionAlreadyConfirmedException
                | TransactionExceptions.InvalidTransactionNotesException ex) {

            // Let GlobalExceptionHandler handle business exceptions
            throw ex;

        } catch (Exception ex) {

            throw new TransactionExceptions.TransactionUpdateException(
                    "Failed to update transaction. Please try again later.");
        }
    }

   public String deleteTransactionMessage(List<Long> transactionIds) {

    try {

        User currentUser = userService.getCurrentUser();

        for (Long transactionId : transactionIds) {

            Transaction transaction = transactionRepo.findById(transactionId)
                    .orElseThrow(() -> new TransactionExceptions.TransactionNotFoundException(
                            "Transaction not found with id: " + transactionId));

            if (!transaction.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new TransactionExceptions.TransactionAccessDeniedException(
                        "You are not allowed to delete transaction with id: " + transactionId);
            }

            transactionRepo.delete(transaction);
        }

        return "Transactions deleted successfully.";

    } catch (TransactionExceptions.TransactionNotFoundException
            | TransactionExceptions.TransactionAccessDeniedException ex) {

        throw ex;

    } catch (Exception ex) {

        throw new TransactionExceptions.TransactionDeleteException(
                "Failed to delete transactions. Please try again later.");
    }
}

}
