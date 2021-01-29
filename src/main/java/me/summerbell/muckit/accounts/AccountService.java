package me.summerbell.muckit.accounts;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.domain.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public Account createNewAccount(Account account){
        //password encoding
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }
}
