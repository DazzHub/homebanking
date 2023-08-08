package com.mindhub.homebanking;

import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return (args) -> {

//			Loan prestamo3 = new Loan("Préstamo Automotriz", 50000, List.of(6,12,24,36));
//
//
//			loanRepository.save(prestamo3);

			createClientMelba(clientRepository, accountRepository, transactionRepository, loanRepository, clientLoanRepository);
			//createClientYo(clientRepository, accountRepository, transactionRepository, clientLoanRepository);
		};
	}

	private void createClientMelba(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		LocalDateTime now =  LocalDateTime.now();
		LocalDateTime sameDayNextDay = now.plusDays(1);
		LocalDateTime sameDayBackDay = now.plusDays(-1);

		Client client = new Client("Melba", "Morel", "melba@mindhub.com");
		clientRepository.save(client);

		Account account = new Account(now, 5000);
		account.setNumber("VIN001");
		Account account2 = new Account(sameDayNextDay,7500);
		account2.setNumber("VIN002");

		Transaction transaction = new Transaction(TransactionType.DEBIT, "Zapatillas", now, 2000);
		Transaction transaction2 = new Transaction(TransactionType.CREDIT, "Joyas", sameDayNextDay, 3000);

		Transaction transaction3 = new Transaction(TransactionType.CREDIT, "Combustible", sameDayBackDay, 4000);
		Transaction transaction4 = new Transaction(TransactionType.DEBIT, "GAS", sameDayBackDay, 15000);

		Loan prestamo1 = new Loan("Préstamo Hipotecario", 500000, List.of(12,24,36,48,60));
		Loan prestamo2 = new Loan("Préstamo Personal", 50000, List.of(6,12,24));

		loanRepository.save(prestamo1);
		loanRepository.save(prestamo2);

		ClientLoan clientLoan1 = new ClientLoan(400000, 60);
		clientLoan1.addLoan(prestamo1);

		ClientLoan clientLoan2 = new ClientLoan(50000, 12);
		clientLoan1.addLoan(prestamo2);

		/* accounts */
		client.addAccount(account);
		client.addAccount(account2);

		accountRepository.save(account);
		accountRepository.save(account2);

		/* loans */
		client.addLoan(clientLoan1);
		client.addLoan(clientLoan2);

		clientLoanRepository.save(clientLoan1);
		clientLoanRepository.save(clientLoan2);

		for (ClientLoan loan : client.getLoans()){
			System.out.println("ID -> " + loan.getLoans().size());
		}

		/* Transactions */
		account.addTransaction(transaction);
		account.addTransaction(transaction2);

		account2.addTransaction(transaction3);
		account2.addTransaction(transaction4);

		transactionRepository.save(transaction);
		transactionRepository.save(transaction2);
		transactionRepository.save(transaction3);
		transactionRepository.save(transaction4);
	}

//	private void createClientYo(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, ClientLoanRepository clientLoanRepository, Loan prestamo1, Loan prestamo2, Loan prestamo3) {
//		LocalDateTime now =  LocalDateTime.now();
//		LocalDateTime sameDayNextDay = now.plusDays(1);
//		LocalDateTime sameDayBackDay = now.plusDays(-1);
//
//		Client client = new Client("Gabriel", "Oubiña", "ak.gabrii@mindhub.com");
//		clientRepository.save(client);
//
//		Account account = new Account(now, 5000);
//		account.setNumber("VIN003");
//		Account account2 = new Account(sameDayNextDay,7500);
//		account2.setNumber("VIN004");
//
//		Transaction transaction = new Transaction(TransactionType.DEBIT, "PC", now, 2000);
//		Transaction transaction2 = new Transaction(TransactionType.CREDIT, "Monitor", sameDayNextDay, 3000);
//
//		Transaction transaction3 = new Transaction(TransactionType.CREDIT, "Combustible", sameDayBackDay, 4000);
//		Transaction transaction4 = new Transaction(TransactionType.DEBIT, "Obra social", sameDayBackDay, 15000);
//
//		ClientLoan clientLoan1 = new ClientLoan(100000, 24, prestamo2);
//		ClientLoan clientLoan2 = new ClientLoan(200000, 36, prestamo3);
//
//		/* accounts */
//		client.addAccount(account);
//		client.addAccount(account2);
//
//		accountRepository.save(account);
//		accountRepository.save(account2);
//
//		/* Loans */
//		client.addLoan(clientLoan1);
//		client.addLoan(clientLoan2);
//
//		clientLoanRepository.save(clientLoan1);
//		clientLoanRepository.save(clientLoan2);
//
//		/* Transactions */
//		account.addTransaction(transaction);
//		account.addTransaction(transaction2);
//
//		account2.addTransaction(transaction3);
//		account2.addTransaction(transaction4);
//
//		transactionRepository.save(transaction);
//		transactionRepository.save(transaction2);
//		transactionRepository.save(transaction3);
//		transactionRepository.save(transaction4);
//	}

}
