package me.summerbell.muckit.accounts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.summerbell.muckit.domain.Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account createNewAccount(Account account){
        //password encoding
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }
}
