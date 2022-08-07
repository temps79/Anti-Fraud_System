package com.example.antifraud.service.impl;

import com.example.antifraud.entity.FraudIP;
import com.example.antifraud.repository.FraudIpRepository;
import com.example.antifraud.service.FraudIpService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FraudIPServiceImpl implements FraudIpService {
    private final FraudIpRepository fraudIpRepository;

    public FraudIPServiceImpl(FraudIpRepository fraudIpRepository) {
        this.fraudIpRepository = fraudIpRepository;
    }

    @Override
    public FraudIP save(FraudIP fraudIP) {
        return fraudIpRepository.save(fraudIP);
    }

    @Override
    public List<FraudIP> findAll() {
        return fraudIpRepository.findAllByOrderById();
    }

    @Override
    public Optional<FraudIP> findFraudIPById(long id) {
        return fraudIpRepository.findById(id);
    }

    @Override
    public Optional<FraudIP> findByIP(String ip) {
        return fraudIpRepository.findByIp(ip);
    }

    @Override
    public void deleteByIp(String ip) throws Exception {
        Optional<FraudIP> optionalFraudIP = fraudIpRepository.findByIp(ip);
        if (optionalFraudIP.isPresent()) {
            fraudIpRepository.delete(optionalFraudIP.get());
        } else {
            throw new Exception("FraudIP not found");
        }
    }

}
