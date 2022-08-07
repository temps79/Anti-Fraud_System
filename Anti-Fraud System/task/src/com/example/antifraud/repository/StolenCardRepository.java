package com.example.antifraud.repository;

import com.example.antifraud.entity.StolenCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StolenCardRepository extends CrudRepository<StolenCard,Long> {
    List<StolenCard> findAllByOrderById();

    Optional<StolenCard> findById(long id);

    Optional<StolenCard> findByNumber(String number);
}
