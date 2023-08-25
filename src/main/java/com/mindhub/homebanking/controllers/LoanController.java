package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private LoanRepository loanRepo;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepo;

    @RequestMapping("/loans")
    public ResponseEntity<Object> getAllLoans(){
        return new ResponseEntity<>(loanRepo.findAll().stream().map(LoanDTO::new).collect(Collectors.toSet()), HttpStatus.OK);
    }

    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> addClientLoans(
            Authentication authentication,
            @RequestBody LoanApplicationDTO loanApplicationDTO
    ) {

        if (loanApplicationDTO == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (authentication == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = getClient(authentication.getName());

        if (client == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Loan loan = loanRepo.findById(loanApplicationDTO.getLoanTypeId()).orElse(null);

        if (loan == null){
            return new ResponseEntity<>("No contamos con ese prestamo", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("No prestamos mas de lo que podemos :c", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(), loan);

        Account accountClient = getAccountByNumber(client, loanApplicationDTO.getToAccountNumber());

        clientLoan.getClients().add(client);
        client.addClientLoan(clientLoan);

        Utils.processTransaction(accountClient, TransactionType.CREDIT, loan.getName(), LocalDateTime.now(), loanApplicationDTO.getAmount(), null);

        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private Client getClient(String email) {
        return clientRepo.findByEmail(email);
    }

    private Account getAccountByNumber(Client client, String accountNumber) {
        return client.getAccounts().stream()
                .filter(account -> account.getNumber().equalsIgnoreCase(accountNumber))
                .findFirst().orElse(null);
    }

}
