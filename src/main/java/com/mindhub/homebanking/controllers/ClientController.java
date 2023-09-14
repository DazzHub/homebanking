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
    private UserDetailsService userDetailsService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;


    @GetMapping("/clients")
    public ResponseEntity<Object> getClients(Authentication authentication){
        if (authentication.getName() == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = clientRepo.findByEmail(authentication.getName());

        if (client == null) return null;

        return new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK);
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClientByID(@PathVariable Long id){
        Client client = clientRepo.findById(id).orElse(null);
        if (client == null) return null;

        return new ClientDTO(client);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,@RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }


        if (clientRepo.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        clientRepo.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/clients/current")
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

    @PostMapping("/clients/current/accounts")
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

        Account account = new Account(now, 0);
        account.setNumber("VIN" + Utils.random3());
        account.setClient(client);
        accountRepository.save(account);

        client.getAccounts().add(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
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

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCardClient(Authentication authentication, @RequestParam TransactionType cardType, @RequestParam CardColor cardColor) {

        if (authentication == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = clientRepo.findByEmail(authentication.getName());

        if (client == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (client.getCards().size() >= 3){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Set<Card> cardsOfType = client.getCards().stream().filter(card -> card.getType().equals(cardType)).collect(Collectors.toSet());

        if (cardsOfType.stream().anyMatch(card -> card.getColor().equals(cardColor))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can NOT have 2 type cards " + cardColor.name());
        }

        Card newCard = new Card(client.getFirstName(), client.getLastName(), cardType, cardColor, LocalDateTime.now());

        newCard.randomCVV();
        newCard.randomCard();
        newCard.setClient(client);

        cardRepository.save(newCard);

        client.getCards().add(newCard);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/cards")
    public ResponseEntity<Object> getCardsClient(Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = clientRepo.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity<>(new ClientDTO(client).getCards(), HttpStatus.OK);
    }
}
