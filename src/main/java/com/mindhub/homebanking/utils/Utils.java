package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.Random;

public class Utils {

    public static String random3() {
        StringBuilder fakeCardNumberBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            fakeCardNumberBuilder.append(random.nextInt(10));
        }

        return fakeCardNumberBuilder.toString();
    }

    public static String fakeCardNumber() {
        StringBuilder fakeCardNumberBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            if (i > 0 && i % 4 == 0) {
                fakeCardNumberBuilder.append("-");
            }
            fakeCardNumberBuilder.append(random.nextInt(10));
        }

        return fakeCardNumberBuilder.toString();
    }

    public static void processTransaction(Account account, TransactionType type, String description, LocalDateTime timestamp, double amount, TransactionRepository transactionRepo) {
        Transaction transaction = new Transaction(type, description, timestamp, amount);
        if (transactionRepo != null){
            transactionRepo.save(transaction);
        }

        if (type == TransactionType.DEBIT) {
            account.removeBalance(amount);
        } else {
            account.addBalance(amount);
        }

        account.addTransaction(transaction);

        transaction.setAccount(account);
    }

}
