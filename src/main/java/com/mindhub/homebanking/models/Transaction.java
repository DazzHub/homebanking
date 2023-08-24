package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.enums.TransactionType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private TransactionType type;
    private String description;
    private LocalDateTime date;

    private double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vin_id")
    private Account account;


    public Transaction(){

    }

    public Transaction(TransactionType type, String description, LocalDateTime date, double amount) {
        this.type = type;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public Transaction(TransactionType type, String description, LocalDateTime date, double amount, Account account) {
        this.type = type;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public TransactionType getType() {
        return type;
    }
}
