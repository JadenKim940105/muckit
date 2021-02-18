package me.summerbell.muckit.kakaosearchapi.kakaomap;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.accounts.kakaologin.KakaoLoginProperties;
import me.summerbell.muckit.domain.Restaurant;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class KakaoSearchService {

    private final KakaoLoginProperties kakaoLoginProperties;

    public ArrayList<Restaurant> keywordSearch(double x, double y){

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

        ResponseEntity response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                kakaoKeywordSearchRequest,
                String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject kakaoData = new JSONObject();
        JSONArray kakaoRestaurantList;

        ArrayList<Restaurant> restaurants = new ArrayList<>();
        try {
             kakaoData = (JSONObject) jsonParser.parse(response.getBody().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        kakaoRestaurantList = (JSONArray) kakaoData.get("documents");
        for(int i = 0 ; i < kakaoRestaurantList.size(); i++){
            JSONObject kakaoRestaurant = (JSONObject) kakaoRestaurantList.get(i);
            Restaurant restaurant = Restaurant.builder()
                    .address_name(kakaoRestaurant.get("address_name").toString())
                    .id(kakaoRestaurant.get("id").toString())
                    .phone(kakaoRestaurant.get("phone").toString())
                    .place_name(kakaoRestaurant.get("place_name").toString())
                    .place_url(kakaoRestaurant.get("place_url").toString())
                    .road_address_name(kakaoRestaurant.get("road_address_name").toString())
                    .place_url(kakaoRestaurant.get("place_url").toString())
                    .longitude(kakaoRestaurant.get("x").toString())
                    .latitude(kakaoRestaurant.get("y").toString())
                    .build();
            restaurants.add(restaurant);
        }



        return restaurants;

    }
}
