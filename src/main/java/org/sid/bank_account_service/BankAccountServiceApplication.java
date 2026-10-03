package org.sid.bank_account_service;

import org.sid.bank_account_service.entities.AccountType;
import org.sid.bank_account_service.entities.BankAccount;
import org.sid.bank_account_service.repositories.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class BankAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(BankAccountRepository repo) {
		return args -> {
			for (int i = 0; i < 4; i++) {
				BankAccount account = BankAccount.builder()
						.createdAt(LocalDate.now())
						.balance(1000 + Math.random() * 9000)
						.currency(i % 2 == 0 ? "MAD" : "EUR")
						.type(i % 2 == 0 ? AccountType.CURRENT_ACCOUNT : AccountType.SAVING_ACCOUNT)
						.build();
				repo.save(account);
			}
			repo.findAll().forEach(a -> {
				System.out.println("=================");
				System.out.println(a.getId());
				System.out.println(a.getBalance());
				System.out.println(a.getCurrency());
				System.out.println(a.getType());
				System.out.println(a.getCreatedAt());
			});
		};
	}
}