package me.summerbell.muckit.accounts;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.utils.kakaologin.KakaoLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/accounts/loginForm")
    public String loginForm() {
        return "login";
    }


    @GetMapping("/accounts/login")
    public String getCode(@RequestParam String code) {
        kakaoLoginService.loginProcess(code);
        return "index";
    }

    @GetMapping("/logintest")
    public String test(){
        return "test";
    }


}
