package me.summerbell.muckit.accounts.kakaologin;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class AccountDto {

    private String accountId;

    private String password;

}
