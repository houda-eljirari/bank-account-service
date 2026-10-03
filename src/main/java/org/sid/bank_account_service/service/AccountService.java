package org.sid.bank_account_service.service;

import org.sid.bank_account_service.dto.BankAccountRequestDTO;
import org.sid.bank_account_service.dto.BankAccountResponseDTO;

import java.util.List;

public interface AccountService {
    BankAccountResponseDTO addAccount(BankAccountRequestDTO dto);
    BankAccountResponseDTO updateAccount(String id, BankAccountRequestDTO dto);
    BankAccountResponseDTO getAccount(String id);
    List<BankAccountResponseDTO> listAccounts();
    void deleteAccount(String id);
}