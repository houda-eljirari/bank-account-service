package org.sid.bank_account_service.service;

import org.sid.bank_account_service.dto.BankAccountRequestDTO;
import org.sid.bank_account_service.dto.BankAccountResponseDTO;
import org.sid.bank_account_service.entities.BankAccount;
import org.sid.bank_account_service.mappers.AccountMapper;
import org.sid.bank_account_service.repositories.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final BankAccountRepository repo;
    private final AccountMapper mapper;

    public AccountServiceImpl(BankAccountRepository repo, AccountMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public BankAccountResponseDTO addAccount(BankAccountRequestDTO dto) {
        BankAccount account = mapper.fromRequestDTO(dto);
        account.setCreatedAt(LocalDate.now());
        return mapper.fromBankAccount(repo.save(account));
    }

    @Override
    public BankAccountResponseDTO updateAccount(String id, BankAccountRequestDTO dto) {
        BankAccount account = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account " + id + " not found"));
        if (dto.getBalance() != null) account.setBalance(dto.getBalance());
        if (dto.getCurrency() != null) account.setCurrency(dto.getCurrency());
        if (dto.getType() != null) account.setType(dto.getType());
        return mapper.fromBankAccount(repo.save(account));
    }

    @Override
    public BankAccountResponseDTO getAccount(String id) {
        return repo.findById(id)
                .map(mapper::fromBankAccount)
                .orElseThrow(() -> new RuntimeException("Account " + id + " not found"));
    }

    @Override
    public List<BankAccountResponseDTO> listAccounts() {
        return repo.findAll().stream().map(mapper::fromBankAccount).toList();
    }

    @Override
    public void deleteAccount(String id) {
        repo.deleteById(id);
    }
}