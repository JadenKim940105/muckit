package me.summerbell.muckit.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Restaurant {

    @Id @GeneratedValue
    @Column(name = "RESTAURANT_ID")
    private Long id;

    private String kakaoId;

    private String place_name;

    private String place_url;

    private String phone;

    private Double longitude;

    private Double latitude;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<LikeIt> likeItList = new ArrayList<>();

    public void addReview(Review review) {
        this.getReviewList().add(review);
        review.setRestaurant(this);
    }
}
