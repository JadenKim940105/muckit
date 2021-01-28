package me.summerbell.muckit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.accounts.AccountRepository;
import me.summerbell.muckit.accounts.AccountSaveService;
import me.summerbell.muckit.domain.Account;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoLoginService implements AccountSaveService {

    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;


    public String loginProcess(String authrizationCode){
        String accessToken = "";
        try {
            accessToken = getAccessToken(authrizationCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String userKakaoId = "";
        try {
            userKakaoId = getUserKakaoId(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userKakaoId;
    }

    public String getAccessToken(String authrizationCode) throws IOException {
        HttpURLConnection connection = setConnectionForToken();
        String response = requestForToken(authrizationCode, connection);
        return response;
    }

    public String getUserKakaoId(String accessToken) throws IOException {
        HttpURLConnection connection = setConnectionForKakaoId(accessToken);
        String response = requestForKakaoId(connection);
        return response;
    }


    private HttpURLConnection setConnectionForToken() throws IOException {
        String requestURL = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }

    private String requestForToken(String authrizationCode, HttpURLConnection connection) throws IOException {
        try(OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())){
            setRequestParamForToken(authrizationCode, writer);
            writer.flush();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                String responseBody = getResponse(br);
                return extractAccessToken(responseBody);
            }
        }
    }

    private void setRequestParamForToken(String authrizationCode, OutputStreamWriter writer) throws IOException {
        writer.append("grant_type=authorization_code" +
                "&client_id=4d27640b59df7c07ae902a2cb443aad6" +
                "&redirect_uri=http://localhost:8080/accounts/login" +
                "&code="+authrizationCode);
    }

    private String extractAccessToken(String responseBody) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }




    private HttpURLConnection setConnectionForKakaoId(String accessToken) throws IOException {
        String requestURL = "https://kapi.kakao.com/v2/user/me";
        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        return connection;
    }

    private String requestForKakaoId(HttpURLConnection connection) throws IOException{
        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String responseBody = getResponse(br);
            return extractKakaoId(responseBody);
        }
    }

    private String extractKakaoId(String responseBody) throws JsonProcessingException {
        return objectMapper.readTree(responseBody).get("id").asText();
    }


    private String getResponse(BufferedReader br) throws IOException {
        String line = "";
        String responseBody = "";

        while ((line=br.readLine())!=null){
            responseBody += line;
        }
        return responseBody;
    }


    @Override
    public Optional<Account> saveAccount(String userNumber) {
        Optional<Account> check = accountRepository.findByUserNumber(userNumber);
        Account savedAccount = check.orElseGet(() -> {
            Account account = Account.builder()
                    .userNumber(userNumber)
                    .createdAt(LocalDateTime.now())
                    .build();
            Account newAccount = accountRepository.save(account);
            return newAccount;
        });
        return Optional.of(savedAccount);
    }
}
