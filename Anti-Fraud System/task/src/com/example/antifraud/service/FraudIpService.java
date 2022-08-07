package com.example.antifraud.service;

import com.example.antifraud.entity.FraudIP;

import java.util.List;
import java.util.Optional;

public interface FraudIpService {
    FraudIP save(FraudIP fraudIP);
    List<FraudIP> findAll();
    Optional<FraudIP> findFraudIPById(long id);

    Optional<FraudIP> findByIP(String ip);
    void deleteByIp(String ip) throws Exception;
}
