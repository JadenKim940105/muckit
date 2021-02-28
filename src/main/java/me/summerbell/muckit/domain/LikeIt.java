package me.summerbell.muckit.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"restaurant"})
public class LikeIt {

    @Id @GeneratedValue
    @Column(name = "LIKE_IT_PK")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RESTAURANT_PK")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_PK")
    private Account account;
}
