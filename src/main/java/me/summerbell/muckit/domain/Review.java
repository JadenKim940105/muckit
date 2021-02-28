package me.summerbell.muckit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "restaurant")
public class Review {

    @Id @GeneratedValue
    @Column(name = "REVIEW_PK")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "RESTAURANT_PK")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_PK")
    private Account account;

    private String reviewContent;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
