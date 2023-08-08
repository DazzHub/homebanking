package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			createClientMelba(clientRepository, accountRepository);
			createClientYo(clientRepository, accountRepository);
		};
	}

	private void createClientMelba(ClientRepository clientRepository, AccountRepository accountRepository) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime sameDayNextDay = now.plusDays(1);

		Client client = new Client("Melba", "Morel", "melba@mindhub.com");
		clientRepository.save(client);

		Account account = new Account(now, 5000);
		account.setNumber("VIN001");
		Account account2 = new Account(sameDayNextDay, 7500);
		account2.setNumber("VIN002");

		client.addAccount(account);
		client.addAccount(account2);

		accountRepository.save(account);
		accountRepository.save(account2);
	}

	private void createClientYo(ClientRepository clientRepository, AccountRepository accountRepository) {
		LocalDateTime now =  LocalDateTime.now();
		LocalDateTime sameDayNextDay = now.plusDays(1);

		Client client = new Client("Gabriel", "Oubiña", "ak.gabrii@mindhub.com");
		clientRepository.save(client);

		Account account = new Account(now, 5000);
		account.setNumber("VIN003");
		Account account2 = new Account(sameDayNextDay,7500);
		account2.setNumber("VIN004");

		client.addAccount(account);
		client.addAccount(account2);

		accountRepository.save(account);
		accountRepository.save(account2);
	}

}
