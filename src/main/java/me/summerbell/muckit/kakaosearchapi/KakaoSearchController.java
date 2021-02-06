package me.summerbell.muckit.kakaosearchapi;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.kakaosearchapi.kakaomap.KakaoSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoSearchController {

    private final KakaoSearchService kakaoSearchService;

    @GetMapping("/find-best-restaurant")
    public String findBestRestaurant(@RequestParam double x, @RequestParam double y){
        String rep = kakaoSearchService.keywordSearch(x, y);
        return rep;
    }
}
