package com.mindhub.homebanking;

import com.mindhub.homebanking.enums.CardColor;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			Loan hipotecario = new Loan("Préstamo Hipotecario", 500000, List.of(12,24,36,48,60));
			Loan personal = new Loan("Préstamo Personal", 100000, List.of(6,12,24));
			Loan automotriz = new Loan("Préstamo Automotriz", 300000, List.of(6,12,24,36));

			loanRepository.save(personal);
			loanRepository.save(hipotecario);
			loanRepository.save(automotriz);

			createClientMelba(clientRepository, accountRepository, transactionRepository, clientLoanRepository, cardRepository, personal, hipotecario, automotriz);
			createClientYo(clientRepository, accountRepository, transactionRepository, clientLoanRepository, cardRepository, personal, hipotecario, automotriz);
		};
	}


	private void createClientMelba(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository, Loan personal, Loan hipotecario, Loan automotriz) {
		LocalDateTime now =  LocalDateTime.now();
		LocalDateTime sameDayNextDay = now.plusDays(1);
		LocalDateTime sameDayBackDay = now.plusDays(-1);

		Client client = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("1234"));
		clientRepository.save(client);

		Account account = new Account(now, 5000);
		account.setNumber("VIN001");
		Account account2 = new Account(sameDayNextDay,7500);
		account2.setNumber("VIN002");

		Transaction transaction = new Transaction(TransactionType.DEBIT, "Zapatillas", now, 2000);
		Transaction transaction2 = new Transaction(TransactionType.CREDIT, "Joyas", sameDayNextDay, 3000);

		Transaction transaction3 = new Transaction(TransactionType.CREDIT, "Combustible", sameDayBackDay, 4000);
		Transaction transaction4 = new Transaction(TransactionType.DEBIT, "GAS", sameDayBackDay, 15000);

		/* accounts */
		client.addAccount(account);
		client.addAccount(account2);

		accountRepository.save(account);
		accountRepository.save(account2);

		/* Transactions */
		account.addTransaction(transaction);
		account.addTransaction(transaction2);

		account2.addTransaction(transaction3);
		account2.addTransaction(transaction4);

		transactionRepository.save(transaction);
		transactionRepository.save(transaction2);
		transactionRepository.save(transaction3);
		transactionRepository.save(transaction4);

		ClientLoan clientLoan = new ClientLoan(400000, 60, hipotecario);
		clientLoan.getClients().add(client);

		ClientLoan clientLoan2 = new ClientLoan(50000, 12, personal);
		clientLoan2.getClients().add(client);

		client.addClientLoan(clientLoan);
		client.addClientLoan(clientLoan2);

		clientLoanRepository.save(clientLoan);
		clientLoanRepository.save(clientLoan2);

		Card card1 = new Card(client.getFirstName(), client.getLastName(), TransactionType.DEBIT, CardColor.GOLD, LocalDateTime.now(), true, true);
		client.addCard(card1);

		cardRepository.save(card1);

		Card card2 = new Card(client.getFirstName(), client.getLastName(), TransactionType.CREDIT, CardColor.TITANIUM, LocalDateTime.now(), true, true);
		client.addCard(card2);
		cardRepository.save(card2);
	}

	private void createClientYo(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository, Loan personal, Loan hipotecario, Loan automotriz) {
		LocalDateTime now =  LocalDateTime.now();
		LocalDateTime sameDayNextDay = now.plusDays(1);
		LocalDateTime sameDayBackDay = now.plusDays(-1);

		Client client = new Client("Gabriel", "Oubiña", "ak.gabrii@gmail.com", passwordEncoder.encode("1234"));
		clientRepository.save(client);

		Account account = new Account(now, 5000);
		account.setNumber("VIN003");
		Account account2 = new Account(sameDayNextDay,7500);
		account2.setNumber("VIN004");

		Transaction transaction = new Transaction(TransactionType.DEBIT, "PC", now, 2000);
		Transaction transaction2 = new Transaction(TransactionType.CREDIT, "Monitor", sameDayNextDay, 3000);

		Transaction transaction3 = new Transaction(TransactionType.CREDIT, "Combustible", sameDayBackDay, 4000);
		Transaction transaction4 = new Transaction(TransactionType.DEBIT, "Obra social", sameDayBackDay, 15000);

		/* accounts */
		client.addAccount(account);
		client.addAccount(account2);

		accountRepository.save(account);
		accountRepository.save(account2);

		/* Transactions */
		account.addTransaction(transaction);
		account.addTransaction(transaction2);

		account2.addTransaction(transaction3);
		account2.addTransaction(transaction4);

		transactionRepository.save(transaction);
		transactionRepository.save(transaction2);
		transactionRepository.save(transaction3);
		transactionRepository.save(transaction4);

		ClientLoan clientLoan = new ClientLoan(100000, 24, personal);
		clientLoan.getClients().add(client);

		ClientLoan clientLoan2 = new ClientLoan(200000, 36, automotriz);
		clientLoan2.getClients().add(client);

		client.addClientLoan(clientLoan);
		client.addClientLoan(clientLoan2);

		clientLoanRepository.save(clientLoan);
		clientLoanRepository.save(clientLoan2);

		Card card1 = new Card(client.getFirstName(), client.getLastName(), TransactionType.CREDIT, CardColor.GOLD, LocalDateTime.now(), true, true);
		client.addCard(card1);

		cardRepository.save(card1);
	}

}
