package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.enums.CardColor;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;


    @RequestMapping("/clients")//solo traer la data del logeado
    public ResponseEntity<Object> getClients(Authentication authentication){
        if (authentication.getName() == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = clientRepo.findByEmail(authentication.getName());

        if (client == null) return null;

        //clientRepo.findAll().stream().map(ClientDTO::new).collect(toList());

        return new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK);
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClientByID(@PathVariable Long id){
        Client client = clientRepo.findById(id).orElse(null);
        if (client == null) return null;

        return new ClientDTO(client);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,@RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepo.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        LocalDateTime now =  LocalDateTime.now();

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        String randomCBU1 = "VIN"+ Utils.random3();
        while (clientRepo.existsByAccountsNumber(randomCBU1)){
            randomCBU1 = "VIN"+ Utils.random3();
        }

        Account account = new Account(now, 0);
        account.setNumber(randomCBU1);

        client.addAccount(account);

        clientRepo.save(client);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping("/clients/current")
    public ResponseEntity<Object> clientCurrent(Authentication authentication) {

        if (authentication.getName() == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = clientRepo.findByEmail(authentication.getName());

        if (client == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new ClientDTO(client), HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccountClient(Authentication authentication) {
        if (authentication == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = clientRepo.findByEmail(authentication.getName());

        if (client == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (client.getAccounts().size() >= 3){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        LocalDateTime now =  LocalDateTime.now();

        String randomCBU1 = "VIN"+ Utils.random3();
        while (clientRepo.existsByAccountsNumber(randomCBU1)){
            randomCBU1 = "VIN"+ Utils.random3();
        }

        Account account = new Account(now, 0);
        account.setNumber(randomCBU1);
        account.setClient(client);
        accountRepository.save(account);

        client.getAccounts().add(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.GET)
    public ResponseEntity<Object> getAccountsClient(Authentication authentication) {

        if (authentication == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = clientRepo.findByEmail(authentication.getName());

        if (client == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new ClientDTO(client).getAccounts(), HttpStatus.OK);
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)//dinamico
    public ResponseEntity<Object> addCardClient(Authentication authentication, @RequestParam TransactionType cardType, @RequestParam CardColor cardColor) {

        if (authentication == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepo.findByEmail(authentication.getName());

        if (client == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Set<Card> cardsOfType = client.getCards().stream().filter(card -> card.getType().equals(cardType)).collect(Collectors.toSet());

        if (cardsOfType.stream().anyMatch(card -> card.getColor().equals(cardColor))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can NOT have 2 type cards " + cardColor.name());
        }

        String numbercard1 = Utils.fakeCardNumber();

        while (cardRepository.existsByNumber(numbercard1)){
            numbercard1 = Utils.fakeCardNumber();
        }

        Card newCard = new Card(client.getFirstName(), client.getLastName(), cardType, cardColor, LocalDateTime.now());
        newCard.randomCVV();
        newCard.randomCard(numbercard1);
        newCard.setClient(client);

        cardRepository.save(newCard);

        client.getCards().add(newCard);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.GET)
    public ResponseEntity<Object> getCardsClient(Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = clientRepo.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        //dto de client para cards
        return new ResponseEntity<>(new ClientDTO(client).getCards(), HttpStatus.OK);
    }
}
