package com.example.antifraud.service;

import com.example.antifraud.entity.Transaction;
import com.example.antifraud.entity.enums.Type;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionService {
    Transaction save(Transaction transaction);
    Map<String,Object> validateTransaction(Transaction transaction);
    Optional<Transaction> findById(Long id);


    Transaction updateFeedback( long id, Type feedback);
    List<Transaction> findAllByOrderById();
    List<Transaction> findAllByNumber(String number);

    void updateLimit(Type validate,Type feedback,long transactionValue);
}
