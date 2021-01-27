package me.summerbell.muckit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/accounts/loginForm")
    public String loginForm() {
        return "/login";
    }

    //todo uri 변경하자 ..
    @GetMapping("/authrizationcode")
    public String getCode(@RequestParam String code) {
        System.out.println(code); // 인가코드
        String accessToken = "";
        try {
            accessToken = kakaoLoginService.getAccessToken(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(accessToken); // accessToken


        String id = "";
        try {
            id = kakaoLoginService.getUserKakaoId(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(id); // db 에 저장할 id


        return "/index";
    }


}
