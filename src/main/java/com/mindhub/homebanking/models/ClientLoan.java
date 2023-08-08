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

    @ManyToMany(fetch = FetchType.EAGER)
    private final Set<Loan> loans = new HashSet<>();

    public ClientLoan(){}

    public ClientLoan(double amount, int payments, Loan loan) {
        this.amount = amount;
        this.payments = payments;
        this.loans.add(loan);
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

    @JsonIgnore
    public Set<Client> getClients() {
        return clients;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public void addClient(Client client) {
        this.clients.add(client);
    }
}
