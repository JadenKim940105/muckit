package me.summerbell.muckit.accounts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class JwtTokenDto {

    private String key;
    private String value;

}
