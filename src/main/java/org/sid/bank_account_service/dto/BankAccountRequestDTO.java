package org.sid.bank_account_service.dto;

import lombok.Data;
import org.sid.bank_account_service.entities.AccountType;

@Data
public class BankAccountRequestDTO {
    private Double balance;
    private String currency;
    private AccountType type;
}