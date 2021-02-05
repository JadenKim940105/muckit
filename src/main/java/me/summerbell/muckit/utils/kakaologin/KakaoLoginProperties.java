package me.summerbell.muckit.utils.kakaologin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties("kakao.login")
public class KakaoLoginProperties {

    private String password;

    private String grantType;

    private String clientId;

    private String redirectUriForAccessToken;

    private String requestUriForAccessToken;

    private String requestUriForUserInfo;
}
