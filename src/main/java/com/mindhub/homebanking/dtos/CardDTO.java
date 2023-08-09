package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.enums.CardColor;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.Card;

import java.time.LocalDateTime;

public class CardDTO {

    private final long id;
    private final String cardHolder;
    private final TransactionType type;
    private final CardColor color;
    private final String number;
    private final int cvv;
    private final LocalDateTime fromDate;
    private final LocalDateTime thruDate;

    public CardDTO(Card card){
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
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
}
