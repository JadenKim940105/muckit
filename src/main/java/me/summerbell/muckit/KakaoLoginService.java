package me.summerbell.muckit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final ObjectMapper objectMapper;

    public String getAccessToken(String authrizationCode) throws IOException {
        HttpURLConnection connection = setConnectionForToken();
        return requestForToken(authrizationCode, connection);
    }

    public String getUserKakaoId(String accessToken) throws IOException {
        HttpURLConnection connection = setConnectionForKakaoId(accessToken);
        return requestForKakaoId(connection);
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
                "&redirect_uri=http://localhost:8080/authrizationcode" +
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
}
