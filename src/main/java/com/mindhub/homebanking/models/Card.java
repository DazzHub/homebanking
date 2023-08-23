package com.mindhub.homebanking.models;

import com.mindhub.homebanking.enums.CardColor;
import com.mindhub.homebanking.enums.TransactionType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String cardHolder;
    private TransactionType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    public Card(){}

    public Card(String firstName, String lastName, TransactionType cardType, CardColor cardColor, LocalDateTime fromDate) {
        this.fromDate = fromDate;
        this.thruDate = fromDate.plusYears(5);

        this.type = cardType;
        this.color = cardColor;

        this.cardHolder = firstName + " " + lastName;
    }

    public void randomCard(){
        StringBuilder fakeCardNumberBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            if (i > 0 && i % 4 == 0) {
                fakeCardNumberBuilder.append("-");
            }
            fakeCardNumberBuilder.append(random.nextInt(10));
        }

        this.number = fakeCardNumberBuilder.toString();
    }

    public void randomCVV(){
        StringBuilder fakeCardNumberBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            fakeCardNumberBuilder.append(random.nextInt(10));
        }

        this.cvv = Integer.parseInt(fakeCardNumberBuilder.toString());
    }

    public Client getClient() {
        return client;
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public TransactionType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
