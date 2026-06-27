package com.whereitgo.utility.parserFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.whereitgo.model.Transaction;
import com.whereitgo.utility.enums.TransactionType;
import com.whereitgo.utility.interfaces.BankSMSParser;

public class SBIParser implements BankSMSParser {

    @Override
    public Transaction parse(String sms) {

        Transaction txn = new Transaction();

        extractAmount(sms, txn);
        extractTransactionType(sms, txn);
        extractDate(sms, txn);
        extractCounterparty(sms, txn);

        txn.setBankName("SBI");

        return txn;
    }

    private void extractAmount(String sms, Transaction txn) {

        Matcher matcher =
                Pattern.compile("by\\s+(\\d+(?:\\.\\d+)?)",
                        Pattern.CASE_INSENSITIVE)
                        .matcher(sms);

        if (matcher.find()) {
            txn.setAmount(new BigDecimal(matcher.group(1)));
        }
    }

    private void extractTransactionType(String sms, Transaction txn) {

        Matcher matcher =
                Pattern.compile("\\b(credited|debited)\\b",
                        Pattern.CASE_INSENSITIVE)
                        .matcher(sms);

        if (matcher.find()) {

            String type = matcher.group(1).toUpperCase();

            if ("DEBITED".equals(type)) {
                txn.setTransactionType(TransactionType.DEBIT);
            } else if ("CREDITED".equals(type)) {
                txn.setTransactionType(TransactionType.CREDIT);
            }
        }
    }

    private void extractDate(String sms, Transaction txn) {

        Matcher matcher =
                Pattern.compile("\\b\\d{2}[A-Za-z]{3}\\d{2}\\b")
                        .matcher(sms);

        if (matcher.find()) {

            String date = matcher.group();

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "ddMMMyy HH:mm:ss",
                            Locale.ENGLISH);

            txn.setTransactionTime(
                    LocalDateTime.parse(
                            date + " 00:00:00",
                            formatter
                    )
            );
        }
    }

    private void extractCounterparty(String sms, Transaction txn) {

        Matcher matcher =
                Pattern.compile(
                        "trf\\s+to\\s+(.*?)\\s+Refno",
                        Pattern.CASE_INSENSITIVE)
                        .matcher(sms);

        if (matcher.find()) {
            txn.setCounterpartyName(matcher.group(1).trim());
        }
    }
}