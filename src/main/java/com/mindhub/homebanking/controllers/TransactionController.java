package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private TransactionRepository transactionRepo;

    //esto seria un try catch?
    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransactions(Authentication authentication, @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam double amount, @RequestParam String description){

        if (fromAccountNumber.isBlank() || fromAccountNumber.isBlank() || toAccountNumber.isBlank() || amount <= 0 || description.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (authentication == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = getClient(authentication.getName());
        Client otherClient = getClientByAccountNumber(toAccountNumber);

        if (client == null || otherClient == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equalsIgnoreCase(toAccountNumber)){
            return new ResponseEntity<>("Same account :V", HttpStatus.FORBIDDEN);
        }

        //buscar cuenta por number
        Account accountClient = getAccountByNumber(client, fromAccountNumber);
        Account accountOtherClient = getAccountByNumber(otherClient, toAccountNumber);

        if (accountClient == null || accountOtherClient == null){
            return new ResponseEntity<>("ou don't have that account :V", HttpStatus.FORBIDDEN);
        }

        if (accountClient.getBalance() <= 0){
            return new ResponseEntity<>("You don't have enough funds :C", HttpStatus.FORBIDDEN);
        }

        LocalDateTime now =  LocalDateTime.now();

        Utils.processTransaction(accountClient, TransactionType.DEBIT, description, now, amount, transactionRepo);
        Utils.processTransaction(accountOtherClient, TransactionType.CREDIT, description, now, amount, transactionRepo);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private Client getClient(String email) {
        return clientRepo.findByEmail(email);
    }

    private Client getClientByAccountNumber(String accountNumber) {
        return clientRepo.findByAccountsNumber(accountNumber);
    }

    private Account getAccountByNumber(Client client, String accountNumber) {
        return client.getAccounts().stream()
                .filter(account -> account.getNumber().equalsIgnoreCase(accountNumber))
                .findFirst().orElse(null);
    }

}
