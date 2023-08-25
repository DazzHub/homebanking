package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private Long loanTypeId;
    private double amount;
    private int payments;
    private String toAccountNumber;

    public LoanApplicationDTO(){

    }

    public LoanApplicationDTO(Long loanTypeId, double amount, int payments, String toAccountNumber) {
        this.loanTypeId = loanTypeId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
    }

    public Long getLoanTypeId() {
        return loanTypeId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
