package me.summerbell.muckit.utils.kakaologin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.accounts.AccountRepository;
import me.summerbell.muckit.accounts.AccountService;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.utils.AccountRole;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoLoginService  {


    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final KakaoLoginProperties kakaoLoginProperties;



    public void loginProcess(String authrizationCode){

        KakaoAccessToken kakaoAccessToken = getAccessToken(authrizationCode);
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken.getAccess_token());
        // 카카오 로그인시 자동으로 DB 등록 (없을시에)
        Account account = saveAccountIfNotExist(kakaoUserInfo);
        // 로그인 처리
        accountService.login(account);

    }

    private Account saveAccountIfNotExist(KakaoUserInfo userInfo) {
        Optional<Account> account = accountRepository.findByEmail(userInfo.getKakao_account().getEmail());
        if(account.isEmpty()){
            Account newAccount = Account.builder()
                    .accountId(userInfo.getKakao_account().getEmail()+"_"+userInfo.getId())
                    .email(userInfo.getKakao_account().getEmail())
                    .password(kakaoLoginProperties.getPassword())
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
        params.add("grant_type",kakaoLoginProperties.getGrantType());
        params.add("client_id",kakaoLoginProperties.getClientId());
        params.add("redirect_uri", kakaoLoginProperties.getRedirectUriForAccessToken());
        params.add("code", authrizationCode);

        //Http header 객체와 Http body 객체를 하나의 오브젝트(Http entity)에 담기
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        // Http 요청하기 (parameter: 요청주소, 요청 method, 요청 header&body 를 담은 httpEntity, 반환값타입)
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoLoginProperties.getRequestUriForAccessToken(),
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
                kakaoLoginProperties.getRequestUriForUserInfo(),
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
