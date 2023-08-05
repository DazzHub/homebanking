package com.mindhub.homebanking;

import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			createClientMelba(clientRepository, accountRepository, transactionRepository);
			createClientYo(clientRepository, accountRepository, transactionRepository);
		};
	}

	private void createClientMelba(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		LocalDateTime now =  LocalDateTime.now();
		LocalDateTime sameDayNextDay = now.plusDays(1);
		LocalDateTime sameDayBackDay = now.plusDays(-1);

		Client client = new Client("Melba", "Morel", "melba@mindhub.com");
		clientRepository.save(client);

		Account account = new Account(now, 5000);
		Account account2 = new Account(sameDayNextDay,7500);

		Transaction transaction = new Transaction(TransactionType.DEBIT, "Zapatillas", now, 2000);
		Transaction transaction2 = new Transaction(TransactionType.CREDIT, "Joyas", sameDayNextDay, 3000);

		Transaction transaction3 = new Transaction(TransactionType.CREDIT, "Combustible", sameDayBackDay, 4000);
		Transaction transaction4 = new Transaction(TransactionType.DEBIT, "GAS", sameDayBackDay, 15000);

		client.addAccount(account);
		client.addAccount(account2);

		accountRepository.save(account);
		accountRepository.save(account2);

		account.addTransaction(transaction);
		account.addTransaction(transaction2);

		account2.addTransaction(transaction3);
		account2.addTransaction(transaction4);

		transactionRepository.save(transaction);
		transactionRepository.save(transaction2);
		transactionRepository.save(transaction3);
		transactionRepository.save(transaction4);
	}

	private void createClientYo(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		LocalDateTime now =  LocalDateTime.now();
		LocalDateTime sameDayNextDay = now.plusDays(1);
		LocalDateTime sameDayBackDay = now.plusDays(-1);

		Client client = new Client("Gabriel", "Oubiña", "ak.gabrii@mindhub.com");
		clientRepository.save(client);

		Account account = new Account(now, 5000);
		Account account2 = new Account(sameDayNextDay,7500);

		Transaction transaction = new Transaction(TransactionType.DEBIT, "PC", now, 2000);
		Transaction transaction2 = new Transaction(TransactionType.CREDIT, "Monitor", sameDayNextDay, 3000);

		Transaction transaction3 = new Transaction(TransactionType.CREDIT, "Combustible", sameDayBackDay, 4000);
		Transaction transaction4 = new Transaction(TransactionType.DEBIT, "Obra social", sameDayBackDay, 15000);

		client.addAccount(account);
		client.addAccount(account2);

		accountRepository.save(account);
		accountRepository.save(account2);

		account.addTransaction(transaction);
		account.addTransaction(transaction2);

		account2.addTransaction(transaction3);
		account2.addTransaction(transaction4);

		transactionRepository.save(transaction);
		transactionRepository.save(transaction2);
		transactionRepository.save(transaction3);
		transactionRepository.save(transaction4);
	}

}
