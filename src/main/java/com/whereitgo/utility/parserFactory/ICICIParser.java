package com.whereitgo.utility.parserFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.whereitgo.exceptionHandling.TransactionExceptions;
import com.whereitgo.model.Transaction;
import com.whereitgo.utility.enums.TransactionType;
import com.whereitgo.utility.interfaces.BankSMSParser;

public class ICICIParser implements BankSMSParser {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(
                    "dd-MMM-yy HH:mm:ss",
                    Locale.ENGLISH);

    @Override
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

        } catch (TransactionExceptions.SMSParsingException |
                 TransactionExceptions.InvalidTransactionDateException |
                 TransactionExceptions.InvalidTransactionAmountException ex) {

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
}