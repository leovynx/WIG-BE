package com.whereitgo.utility.parserFactory;

import com.whereitgo.exceptionHandling.TransactionExceptions;
import com.whereitgo.exceptionHandling.UserExceptions;
import com.whereitgo.utility.interfaces.BankSMSParser;

public class ParserFactory {

    public static BankSMSParser getParser(String sms) {

        if (sms.contains("ICICI Bank")) {
            return new ICICIParser();
        }

        if (sms.contains("-SBI") || sms.contains("SBI")) {
            return new SBIParser();
        }

        throw new TransactionExceptions.UnsupportedBankException(
                    "We are not supporting this bank");
    }
}
