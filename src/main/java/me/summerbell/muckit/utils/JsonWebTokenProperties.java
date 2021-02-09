package me.summerbell.muckit.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties("json.web.token")
public class JsonWebTokenProperties {

    private String tokenSubject;

    private int tokenExpireTime;

    private String tokenSecure;
}
