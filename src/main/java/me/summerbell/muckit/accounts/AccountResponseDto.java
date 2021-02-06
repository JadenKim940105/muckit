package me.summerbell.muckit.accounts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class AccountResponseDto {

    private String accountId;

    private String nickName;

    private String email;

}
