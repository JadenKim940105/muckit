package me.summerbell.muckit.accounts;

import me.summerbell.muckit.domain.Account;

import java.util.Optional;

public interface AccountSaveService {

    Optional<Account> saveAccount(String userNumber);
}
