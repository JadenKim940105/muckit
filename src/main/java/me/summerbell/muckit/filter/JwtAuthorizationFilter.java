package me.summerbell.muckit.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import me.summerbell.muckit.accounts.AccountRepository;
import me.summerbell.muckit.accounts.auth.PrincipalDetail;
import me.summerbell.muckit.domain.Account;
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
 만약, 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않는다.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private AccountRepository accountRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AccountRepository accountRepository) {
        super(authenticationManager);
        this.accountRepository = accountRepository;
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
        String accountId = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("id").asString();

        // 서명이 정상적으로 됨
        if(accountId != null){
            Optional<Account> account = accountRepository.findByAccountId(accountId);
            System.out.println("accountID2 " + accountId);

            // todo 코드수정 필요
            PrincipalDetail principalDetail = new PrincipalDetail(account.get());

            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetail, null, principalDetail.getAuthorities());

            // SecurityContextHolder 에 직접 Authentication 객체 주입 == 로그인 완료
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        chain.doFilter(request, response);


    }
}
