package me.summerbell.muckit.kakaosearchapi.kakaomap;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.accounts.kakaologin.KakaoLoginProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoSearchService {

    private final KakaoLoginProperties kakaoLoginProperties;

    public String keywordSearch(double x, double y){

        RestTemplate restTemplate = new RestTemplate();

        // Http header 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK "+kakaoLoginProperties.getClientId());
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8");

        // 요청객체
        HttpEntity<MultiValueMap<String, String>> kakaoKeywordSearchRequest = new HttpEntity<>(headers);

        // 요청 파라미터
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com//v2/local/search/category.json")
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("radius", 2000)
                .queryParam("category_group_code", "FD6");


        // 요청 후 응답
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                kakaoKeywordSearchRequest,
                String.class
        );

        return response.getBody();

    }
}
