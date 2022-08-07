package com.example.antifraud.service.impl;

import com.example.antifraud.entity.StolenCard;
import com.example.antifraud.repository.StolenCardRepository;
import com.example.antifraud.service.StolenCardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StolenCardServiceImpl implements StolenCardService {
    private final StolenCardRepository stolenCardRepository;

    public StolenCardServiceImpl(StolenCardRepository stolenCardRepository) {
        this.stolenCardRepository = stolenCardRepository;
    }

    @Override
    public StolenCard save(StolenCard stolenCard) {
        return stolenCardRepository.save(stolenCard);
    }

    @Override
    public List<StolenCard> findAll() {
        return stolenCardRepository.findAllByOrderById();
    }

    @Override
    public Optional<StolenCard> findById(long id) {
        return stolenCardRepository.findById(id);
    }

    @Override
    public Optional<StolenCard> findByNumber(String number) {
        return stolenCardRepository.findByNumber(number);
    }

    @Override
    public void deleteByNumber(String number) throws Exception {
        Optional<StolenCard> optionalStolenCard = stolenCardRepository.findByNumber(number);
        if (optionalStolenCard.isPresent()) {
            stolenCardRepository.delete(optionalStolenCard.get());
        } else {
            throw new Exception("StolenCard not found");
        }
    }
}
