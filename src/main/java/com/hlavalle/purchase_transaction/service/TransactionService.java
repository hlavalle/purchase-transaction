package com.hlavalle.purchase_transaction.service;

import com.hlavalle.purchase_transaction.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);

    List<Transaction> getAllTransactions();
}
