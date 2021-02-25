package me.summerbell.muckit.accounts.auth;

import lombok.Data;
import me.summerbell.muckit.domain.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class AccountUser implements UserDetails {

    private Account account;

    public AccountUser(Account account) {
        this.account = account;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getAccountId();
    }

    // 계정만료 (true 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정락 (true 락 안걸려있음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 (true 만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정활성화 ( true 활성화 )
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> "ROLE_" + account.getRole());
        return authorities;
    }
}
