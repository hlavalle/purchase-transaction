package com.hlavalle.purchase_transaction.service;

import com.hlavalle.purchase_transaction.entity.Transaction;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);

    Transaction convertCurrency(String transactionId, String currency);

}
