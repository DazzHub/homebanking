package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double amount;
    private int payments;

    @ManyToMany(fetch = FetchType.EAGER)
    private final Set<Client> clients = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Loan loan;

    public ClientLoan(){}

    public ClientLoan(double amount, int payments, Loan loan) {
        this.amount = amount;
        this.payments = payments;
        this.loan = loan;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public Loan getLoan() {
        return loan;
    }

}
