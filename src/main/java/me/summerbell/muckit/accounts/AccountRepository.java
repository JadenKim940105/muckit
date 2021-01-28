package me.summerbell.muckit.accounts;

import me.summerbell.muckit.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserNumber(String userNumber);
}
