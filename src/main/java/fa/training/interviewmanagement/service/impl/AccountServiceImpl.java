package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entities.Account;
import fa.training.interviewmanagement.repository.AccountRepository;
import fa.training.interviewmanagement.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    final private AccountRepository accountRepository;
    @Override
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}
