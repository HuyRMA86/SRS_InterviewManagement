package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entities.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> findAccountByEmail(String email);
}
