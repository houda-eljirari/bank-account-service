package org.sid.bank_account_service.mappers;

import org.sid.bank_account_service.dto.BankAccountRequestDTO;
import org.sid.bank_account_service.dto.BankAccountResponseDTO;
import org.sid.bank_account_service.entities.BankAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public BankAccountResponseDTO fromBankAccount(BankAccount bankAccount) {
        BankAccountResponseDTO dto = new BankAccountResponseDTO();
        BeanUtils.copyProperties(bankAccount, dto);
        return dto;
    }

    public BankAccount fromRequestDTO(BankAccountRequestDTO dto) {
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(dto, bankAccount);
        return bankAccount;
    }
}