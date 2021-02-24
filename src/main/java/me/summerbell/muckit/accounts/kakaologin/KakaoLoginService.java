package me.summerbell.muckit.accounts.kakaologin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.summerbell.muckit.accounts.AccountRepository;
import me.summerbell.muckit.accounts.AccountService;
import me.summerbell.muckit.accounts.JwtTokenDto;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.utils.AccountRole;
import me.summerbell.muckit.utils.JsonWebTokenProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginService  {


    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final KakaoProperties kakaoProperties;
    private final JsonWebTokenProperties jsonWebTokenProperties;


    public JwtTokenDto loginApiProcess(KakaoAccessToken accessToken){
        // 카카오에서 유저정보 가져오기
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken.getAccess_token());
        // jwt 만들기 위한 정보 DB에서 가져오기
        Account account = saveAccountIfNotExist(kakaoUserInfo);
        log.info(account.getNickName() + " 저장 완료 ");
        // jwt 토큰 생성
        JwtTokenDto jwtToken = createJwtToken(account);
        return jwtToken;
    }


    public JwtTokenDto loginProcess(String authorizationCode){
        KakaoAccessToken kakaoAccessToken = getAccessToken(authorizationCode);
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken.getAccess_token());
        // 카카오 로그인시 자동으로 DB 등록 (없을시에)
        Account account = saveAccountIfNotExist(kakaoUserInfo);
        log.info(account.getNickName() + " 저장 완료 ");

        // jwt 토큰 생성
        JwtTokenDto jwtToken = createJwtToken(account);
        return jwtToken;
    }

    private JwtTokenDto createJwtToken(Account account) {
        String token = JWT.create()
                .withSubject(jsonWebTokenProperties.getTokenSubject()) // tokenSubject
                .withExpiresAt(new Date(System.currentTimeMillis() + jsonWebTokenProperties.getTokenExpireTime())) // tokenExpireTime
                .withClaim("id", account.getAccountId())
                .sign(Algorithm.HMAC512(jsonWebTokenProperties.getTokenSecure()));

        JwtTokenDto tokenDto = JwtTokenDto.builder()
                .key("Authorization")
                .value("Bearer " + token)
                .build();

        return tokenDto;
    }

    // 가져온 카카오유저정보를 서버의 DB와 비교해 없다면, 저장 후 저장된 Account Return
    // 이미 저장되어있는 정봐면 DB 에서 가져온 Account Return
    private Account saveAccountIfNotExist(KakaoUserInfo userInfo) {
        Optional<Account> account = accountRepository.findByAccountId(userInfo.getKakao_account().getEmail()+"_"+userInfo.getId());
        if(account.isEmpty()){
            Account newAccount = Account.builder()
                    .accountId(userInfo.getKakao_account().getEmail()+"_"+userInfo.getId())
                    .email(userInfo.getKakao_account().getEmail())
                    .password(kakaoProperties.getPassword())
                    .createdAt(LocalDateTime.now())
                    .isOauth(true)
                    .role(AccountRole.USER)
                    .nickName(userInfo.properties.getNickname())
                    .build();
            accountService.createNewAccount(newAccount);
            return newAccount;
        } else {
            return account.get();
        }
    }

    private KakaoAccessToken getAccessToken(String authrizationCode){

        RestTemplate restTemplate = new RestTemplate();

        //Http header 객체생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //Http body 객체생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", kakaoProperties.getGrantType());
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUriForAccessToken());
        params.add("code", authrizationCode);

        //Http header 객체와 Http body 객체를 하나의 오브젝트(Http entity)에 담기
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        // Http 요청하기 (parameter: 요청주소, 요청 method, 요청 header&body 를 담은 httpEntity, 반환값타입)
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoProperties.getRequestUriForAccessToken(),
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        // response 받은 json 데이터 매핑
        KakaoAccessToken kakaoAccessToken = null;
        try {
            kakaoAccessToken = objectMapper.readValue(response.getBody(), KakaoAccessToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoAccessToken;
    }
    // 카카오 유저정보
    private KakaoUserInfo getKakaoUserInfo(String accessToken){

        RestTemplate restTemplate = new RestTemplate();

        // Http header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        // 요청객체(HttpEntity)
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        // 요청( restTemplate.exchange() ) 후 응답받기(ResponseEntity)
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoProperties.getRequestUriForUserInfo(),
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        KakaoUserInfo kakaoUserInfo = null;
        try {
            kakaoUserInfo = objectMapper.readValue(response.getBody(), KakaoUserInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoUserInfo;
    }
}
