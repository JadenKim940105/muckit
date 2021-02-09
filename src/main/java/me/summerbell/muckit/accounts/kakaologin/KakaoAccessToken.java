package me.summerbell.muckit.accounts.kakaologin;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class KakaoAccessToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;
}
