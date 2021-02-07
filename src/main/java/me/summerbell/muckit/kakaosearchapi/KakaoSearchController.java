package me.summerbell.muckit.kakaosearchapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.summerbell.muckit.kakaosearchapi.kakaomap.KakaoSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoSearchController {

    private final KakaoSearchService kakaoSearchService;

    @GetMapping("/find-best-restaurant")
    public String findBestRestaurant(@RequestParam double x, @RequestParam double y, Principal principal){
        log.info(principal.getName());
        String rep = kakaoSearchService.keywordSearch(x, y);
        return rep;
    }
}
