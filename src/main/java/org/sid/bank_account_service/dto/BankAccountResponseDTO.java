package org.sid.bank_account_service.dto;

import lombok.Data;
import org.sid.bank_account_service.entities.AccountType;

import java.time.LocalDate;

@Data
public class BankAccountResponseDTO {
    private String id;
    private LocalDate createdAt;
    private Double balance;
    private String currency;
    private AccountType type;
}