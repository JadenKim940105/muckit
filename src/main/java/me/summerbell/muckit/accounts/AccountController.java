package me.summerbell.muckit.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.accounts.kakaologin.KakaoLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final KakaoLoginService kakaoLoginService;
    private final ObjectMapper objectMapper;


    @GetMapping("/accounts/loginForm")
    public String loginForm() {
        return "login";
    }


    @GetMapping("/accounts/login")
    @ResponseBody
    public String getCode(@RequestParam String code) {
        AccountResponseDto accountResponseDto = kakaoLoginService.loginProcess(code);
        String response = "";
        try {
             response = objectMapper.writeValueAsString(accountResponseDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/logintest")
    public String test(){
        return "test";
    }


}
