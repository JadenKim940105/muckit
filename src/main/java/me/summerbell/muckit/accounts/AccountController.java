package me.summerbell.muckit.accounts;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.accounts.kakaologin.KakaoAccessToken;
import me.summerbell.muckit.accounts.kakaologin.KakaoLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class AccountController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/accounts/loginForm")
    public String loginForm() {
        return "login";
    }


    @GetMapping("/accounts/login")
    @ResponseBody
    public JwtTokenDto getToken(@RequestParam String code) {
        JwtTokenDto tokenDto = kakaoLoginService.loginProcess(code);
        return tokenDto;
    }

    @PostMapping("/accounts/api/login")
    @ResponseBody
    public ResponseEntity<JwtTokenDto> createToken(@RequestBody KakaoAccessToken accessToken){

        JwtTokenDto tokenDto = kakaoLoginService.loginApiProcess(accessToken);
        return ResponseEntity.ok(tokenDto);

    }
}
