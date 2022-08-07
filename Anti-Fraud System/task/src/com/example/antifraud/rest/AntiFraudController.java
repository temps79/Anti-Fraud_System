package com.example.antifraud.rest;

import com.example.antifraud.dto.FeedBack;
import com.example.antifraud.entity.FraudIP;
import com.example.antifraud.entity.StolenCard;
import com.example.antifraud.entity.Transaction;
import com.example.antifraud.service.FraudIpService;
import com.example.antifraud.service.StolenCardService;
import com.example.antifraud.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/antifraud")
public class AntiFraudController {
    private final FraudIpService fraudIpService;
    private final StolenCardService stolenCardService;
    private final TransactionService transactionService;

    public AntiFraudController(FraudIpService fraudIpService, StolenCardService stolenCardService, TransactionService transactionService) {
        this.fraudIpService = fraudIpService;
        this.stolenCardService = stolenCardService;
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> getTransaction(@RequestBody Transaction transaction) {
        if (transaction.getType() == null || transaction.getDate()==null || transaction.getRegion()==null) return ResponseEntity.badRequest().build();
        Map<String,Object> resultMap=transactionService.validateTransaction(transaction);
        return ResponseEntity.ok(resultMap);
    }
    @PutMapping("/transaction")
    public ResponseEntity<?> updateFeedback(@RequestBody FeedBack feedBack) {
        Optional<Transaction> optionalTransaction=transactionService.findById(feedBack.getTransactionId());
        if(optionalTransaction.isPresent()){
            Transaction transaction=optionalTransaction.get();
            if(transaction.getType()==feedBack.getFeedback()){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if(transaction.getFeedback()!=null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "The feedback for this transaction is already in the database.");
            }
            if (feedBack.getFeedback()==null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad credentials");
            }
            Transaction updateTransaction=transactionService.updateFeedback(feedBack.getTransactionId(),feedBack.getFeedback());
            transactionService.updateLimit(updateTransaction.getType(),feedBack.getFeedback(),updateTransaction.getAmount());
            return ResponseEntity.ok(updateTransaction);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction is not found in history.");
    }

    @GetMapping(value={"/history/{number}","/history"})
    public ResponseEntity<?> getHistory(@PathVariable(required = false, name = "number") String number) {

        List<Transaction> transactionList;
        if(number!=null && !number.isEmpty()){
            if(!StolenCard.isValidLuhn(number)) return ResponseEntity.badRequest().build();
            transactionList=transactionService.findAllByNumber(number);
            if(transactionList.size()==0) return ResponseEntity.notFound().build();

        }else{
            transactionList=transactionService.findAllByOrderById();
        }
        return ResponseEntity.ok(transactionList);
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<?> suspiciousIp(@RequestBody FraudIP fraudIP) {
        Optional<FraudIP> findFraudIP = fraudIpService.findByIP(fraudIP.getIp());
        if (findFraudIP.isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        if (fraudIP.getIp() == null) return ResponseEntity.badRequest().build();
        FraudIP newFraudIP = fraudIpService.save(fraudIP);
        return ResponseEntity.ok(newFraudIP);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<?> deleteSuspiciousIp(@PathVariable("ip") String ip) {
        try {
            if (!FraudIP.checkIP(ip)) return ResponseEntity.badRequest().build();
            fraudIpService.deleteByIp(ip);
            return ResponseEntity.ok(Map.of("status", String.format("IP %s successfully removed!", ip)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<?> getSuspiciousIp() {
        return ResponseEntity.ok(fraudIpService.findAll());
    }

    @PostMapping("/stolencard")
    public ResponseEntity<?> saveStolenCard(@RequestBody StolenCard stolenCard) {
        if(!StolenCard.isValidLuhn(stolenCard.getNumber())) return ResponseEntity.badRequest().build();
        Optional<StolenCard> optionalStolenCard = stolenCardService.findByNumber(stolenCard.getNumber());
        if (optionalStolenCard.isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        if (stolenCard.getNumber() == null) return ResponseEntity.badRequest().build();
        StolenCard newStolenCard = stolenCardService.save(stolenCard);
        return ResponseEntity.ok(newStolenCard);
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<?> deleteStolenCard(@PathVariable("number") String number) {
        try {
            if(!StolenCard.isValidLuhn(number)) return ResponseEntity.badRequest().build();
            stolenCardService.deleteByNumber(number);
            return ResponseEntity.ok(Map.of("status", String.format("Card %s successfully removed!", number)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/stolencard")
    public ResponseEntity<?> getSStolenCard() {
        return ResponseEntity.ok(stolenCardService.findAll());
    }
}
