package me.summerbell.muckit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"reviewList", "likeItList"})
public class Restaurant {

    @Id @GeneratedValue
    @Column(name = "RESTAURANT_ID")
    private Long id;

    private String addressName;

    private String kakaoId;

    private String phone;

    private String placeName;

    private String placeUrl;

    private String roadAddressName;

    private String longitude;

    private String latitude;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<LikeIt> likeItList = new ArrayList<>();

    // todo Review 가 관계의 주인.. 확인 후 현재 코드삭제
    public void addReview(Review review) {
        this.getReviewList().add(review);
        review.setRestaurant(this);
    }
}
