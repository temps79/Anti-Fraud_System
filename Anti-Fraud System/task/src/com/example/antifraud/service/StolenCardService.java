package com.example.antifraud.service;



import com.example.antifraud.entity.StolenCard;

import java.util.List;
import java.util.Optional;

public interface StolenCardService {
    StolenCard save(StolenCard stolenCard);
    List<StolenCard> findAll();
    Optional<StolenCard> findById(long id);

    Optional<StolenCard> findByNumber(String number);
    void deleteByNumber(String number) throws Exception;
}
