package org.sid.bank_account_service.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "web", types = BankAccount.class)
public interface AccountProjection2 {
    String getId();
    Double getBalance();
    String getCurrency();
    AccountType getType();
}