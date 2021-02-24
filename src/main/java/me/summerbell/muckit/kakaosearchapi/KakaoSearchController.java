package me.summerbell.muckit.kakaosearchapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.summerbell.muckit.domain.Restaurant;
import me.summerbell.muckit.kakaosearchapi.kakaomap.LocationDto;
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

    @GetMapping(value = "/find-best-restaurant", produces = MediaTypes.HAL_JSON_VALUE+";charset=UTF-8")
    public ResponseEntity findBestRestaurant(@RequestParam double longitude,
                                             @RequestParam double latitude){

        ArrayList<Restaurant> tests = kakaoSearchService.locationSearch(longitude, latitude);

        return ResponseEntity.ok(tests);
    }

    // todo keyword(ex: 강남역)을 쳤을 경우 해당 키워드에 해당하는 위치정보 반환
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
