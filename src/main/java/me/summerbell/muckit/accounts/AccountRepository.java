package me.summerbell.muckit.accounts;

import me.summerbell.muckit.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);


    Optional<Account> findByAccountId(String accountId);
}
