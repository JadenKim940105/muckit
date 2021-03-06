package me.summerbell.muckit.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import me.summerbell.muckit.accounts.AccountRepository;
import me.summerbell.muckit.accounts.auth.AccountUser;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.utils.JsonWebTokenProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/*
 security filter 중 BasicAuthenticationFilter 은 권한이나, 인증이 필요한 특정 주소를 요청할 경우 타는 필터이다.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private AccountRepository accountRepository;
    private JsonWebTokenProperties jsonWebTokenProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AccountRepository accountRepository, JsonWebTokenProperties jsonWebTokenProperties) {
        super(authenticationManager);
        this.accountRepository = accountRepository;
        this.jsonWebTokenProperties = jsonWebTokenProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader("Authorization");

        // header 가 있는지 확인
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request, response);
            return;
        }
        // jwt 검증해서 정상사용자인지 확인
        String jwtToken = request.getHeader("Authorization").replace("Bearer ","");

        String accountId = JWT.require(Algorithm.HMAC512(jsonWebTokenProperties.getTokenSecure())).build().verify(jwtToken).getClaim("id").asString();

        // 서명이 정상적으로 됨
        if(accountId != null){
            Optional<Account> account = accountRepository.findByAccountId(accountId);

            AccountUser accountUser = new AccountUser(account.get());

            Authentication authentication = new UsernamePasswordAuthenticationToken(accountUser, null, accountUser.getAuthorities());

            // SecurityContextHolder 에 직접 Authentication 객체 주입
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
