package com.example.antifraud.service.impl;

import com.example.antifraud.dto.Limit;
import com.example.antifraud.entity.Transaction;
import com.example.antifraud.entity.enums.Type;
import com.example.antifraud.repository.TransactionRepository;
import com.example.antifraud.service.FraudIpService;
import com.example.antifraud.service.StolenCardService;
import com.example.antifraud.service.TransactionService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final FraudIpService fraudIpService;
    private final StolenCardService stolenCardService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, FraudIpService fraudIpService, StolenCardService stolenCardService) {
        this.transactionRepository = transactionRepository;
        this.fraudIpService = fraudIpService;
        this.stolenCardService = stolenCardService;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Map<String, Object> validateTransaction(Transaction transaction) {
        Timestamp from=new Timestamp(transaction.getDate().getTime()- TimeUnit.HOURS.toMillis(1));
        Timestamp to=new Timestamp(transaction.getDate().getTime());

        List<Transaction> transactionList = transactionRepository.findAllByDateBetween(from, to);

        transaction.stolenCardValidate(stolenCardService,transactionList);
        transaction.ipValidate(fraudIpService,transactionList);
        transaction.regionValidate(transactionList);

        save(transaction);
        return Map.of("result", transaction.getType(), "info", transaction.getInfo());
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction updateFeedback(long id, Type feedback) {
        transactionRepository.updateFeedback(id,feedback);
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> findAllByOrderById() {
        return transactionRepository.findAllByOrderById();
    }

    @Override
    public List<Transaction> findAllByNumber(String number) {
        return transactionRepository.findAllByNumber(number);
    }

    @Override
    public void updateLimit(Type validate, Type feedback,long transactionValue) {
        if(validate==Type.ALLOWED){
            if(feedback==Type.MANUAL_PROCESSING){
                Limit.getInstance().reduceLimitAllowed(transactionValue);
            }else{
                Limit.getInstance().reduceLimitAllowed(transactionValue);
                Limit.getInstance().reduceLimitManual(transactionValue);
            }
        }
        else if(validate==Type.MANUAL_PROCESSING){
            if(feedback==Type.ALLOWED){
                Limit.getInstance().increaseLimitAllowed(transactionValue);
            }else{
                Limit.getInstance().reduceLimitManual(transactionValue);
            }
        }else if(validate==Type.PROHIBITED){
            if(feedback==Type.ALLOWED){
                Limit.getInstance().increaseLimitAllowed(transactionValue);
                Limit.getInstance().increaseLimitManual(transactionValue);
            }else{
                Limit.getInstance().increaseLimitManual(transactionValue);
            }
        }
    }


}
