package com.example.antifraud.repository;

import com.example.antifraud.entity.FraudIP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FraudIpRepository extends CrudRepository< FraudIP,Long> {
    List<FraudIP> findAllByOrderById();

    Optional<FraudIP> findById(long id);

    Optional<FraudIP> findByIp(String ip);

}
