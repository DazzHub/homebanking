package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientLoanDTO {

    private final long id;
    private final double amount;
    private final int payments;

    private final Set<ClientDTO> clients;

    private final Set<LoanDTO> loans;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.clients = clientLoan.getClients().stream().map(ClientDTO::new).collect(Collectors.toSet());
        this.loans = clientLoan.getLoans().stream().map(LoanDTO::new).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public Set<ClientDTO> getClients() {
        return clients;
    }

    public Set<LoanDTO> getLoans() {
        return loans;
    }
}
