package com.example.antifraud.repository;

import com.example.antifraud.entity.Transaction;
import com.example.antifraud.entity.enums.Type;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@EnableJpaRepositories
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Transaction save(Transaction transaction);
    @Transactional
    List<Transaction> findAllByDateBetween(Timestamp fromDate,  Timestamp  toDate);

    List<Transaction> findAllByOrderById();
    List<Transaction> findAllByNumber(String number);
    @Modifying(clearAutomatically = true)
    @Query("update Transaction t set t.feedback =:feedback where t.id=:id")
    @Transactional
    void updateFeedback(@Param(value = "id") long id, @Param(value = "feedback") Type feedback);
}
