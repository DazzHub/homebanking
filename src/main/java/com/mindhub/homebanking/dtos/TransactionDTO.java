package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;

public class TransactionDTO {

    private final long id;
    private final TransactionType type;
    private final String description;
    private final LocalDateTime date;
    private final double amount;

    public TransactionDTO(Transaction transaction){
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.amount = transaction.getAmount();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}
