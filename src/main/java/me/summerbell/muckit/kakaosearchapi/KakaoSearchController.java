package me.summerbell.muckit.kakaosearchapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.summerbell.muckit.kakaosearchapi.dto.LocationDto;
import me.summerbell.muckit.kakaosearchapi.dto.RestaurantDto;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoSearchController {

    private final KakaoSearchService kakaoSearchService;

    // 경도,위도를 받아와 그 주변의 음식점 리스트 반환
    @GetMapping(value = "/find-best-restaurant", produces = MediaTypes.HAL_JSON_VALUE+";charset=UTF-8")
    public ResponseEntity findBestRestaurant(@RequestParam double longitude,
                                             @RequestParam double latitude){


        ArrayList<RestaurantDto> tests = kakaoSearchService.locationSearch(longitude, latitude);

        return ResponseEntity.ok(tests);
    }

    // 키워드로 검색한 지역의 경도, 위도 반환
    @GetMapping("/search-location")
    public ResponseEntity searchLocationByKeyword(@RequestParam String keyword){
        LocationDto locationDto = new LocationDto();
        try {
            locationDto = kakaoSearchService.findLocationByKeyword(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(locationDto);
    }
}
