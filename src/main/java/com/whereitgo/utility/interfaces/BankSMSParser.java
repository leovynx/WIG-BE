package com.whereitgo.utility.interfaces;

import com.whereitgo.model.Transaction;

public interface BankSMSParser {
    Transaction parse(String sms);
}
