package me.summerbell.muckit.accounts.auth;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.accounts.AccountRepository;
import me.summerbell.muckit.domain.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
//todo AccountAuthService 로 이름 변경 해보는 것 고려..
public class PrincipalDetailService implements UserDetailsService {


    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountId(accountId).orElseThrow(
                ()-> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. " + accountId));
        return new PrincipalDetail(account);
    }
}
