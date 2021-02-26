package me.summerbell.muckit.domain;

import lombok.*;
import me.summerbell.muckit.utils.AccountRole;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    @Column(name = "ACCOUNT_PK")
    private Long id;

    @Column(unique = true)
    private String accountId;

    @Column(nullable = false)
    private String password;

    private String nickName;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private AccountRole role;

    private LocalDateTime createdAt;

    private boolean isOauth;

}
