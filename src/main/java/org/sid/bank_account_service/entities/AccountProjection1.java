package org.sid.bank_account_service.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "mobile", types = BankAccount.class)
public interface AccountProjection1 {
    String getId();
    Double getBalance();
}