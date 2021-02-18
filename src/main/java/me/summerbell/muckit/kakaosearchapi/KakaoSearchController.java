package me.summerbell.muckit.kakaosearchapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.summerbell.muckit.domain.Restaurant;
import me.summerbell.muckit.kakaosearchapi.kakaomap.KakaoSearchService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoSearchController {

    private final KakaoSearchService kakaoSearchService;

    @GetMapping(value = "/find-best-restaurant", produces = MediaTypes.HAL_JSON_VALUE+";charset=UTF-8")
    public ResponseEntity findBestRestaurant(@RequestParam double longitude,
                                             @RequestParam double latitude){

        ArrayList<Restaurant> tests = kakaoSearchService.keywordSearch(longitude, latitude);


        return ResponseEntity.ok(tests);
    }
}
