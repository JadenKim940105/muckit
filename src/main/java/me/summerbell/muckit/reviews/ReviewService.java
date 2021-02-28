package me.summerbell.muckit.reviews;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.domain.Restaurant;
import me.summerbell.muckit.domain.Review;
import me.summerbell.muckit.kakaosearchapi.dto.RestaurantDto;
import me.summerbell.muckit.restaurants.RestaurantRepository;
import me.summerbell.muckit.utils.vo.RestaurantReviewVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final RestaurantRepository restaurantRepository;

    public List<Review> getReview(String restaurantKakaoId) {
        Optional<Restaurant> byKakaoId = restaurantRepository.findByKakaoId(restaurantKakaoId);
        byKakaoId.orElseThrow(NoSuchElementException::new);
        List<Review> reviewList = byKakaoId.get().getReviewList();
        return reviewList;
    }

    public Restaurant saveRestaurantInfo(RestaurantReviewVo vo) {
        Restaurant newRestaurant = createNewRestaurant(vo);
        return restaurantRepository.save(newRestaurant);
    }

    private Restaurant createNewRestaurant(RestaurantReviewVo vo) {
        Restaurant restaurant = Restaurant.builder()
                .addressName(vo.getAddress_name())
                .phone(vo.getPhone())
                .kakaoId(vo.getKakao_id())
                .latitude(vo.getLatitude())
                .longitude(vo.getLongitude())
                .placeName(vo.getPlace_name())
                .placeUrl(vo.getPlace_url())
                .roadAddressName(vo.getRoad_address_name())
                .build();
        return restaurant;
    }

    public ReviewDto createReview(Restaurant restaurant, RestaurantReviewVo vo, Account account) {
        Review newReview = Review.builder()
                .account(account)
                .reviewContent(vo.getReviewContent())
                .restaurant(restaurant)
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(newReview);

        ReviewDto responseReviewDto = ReviewDto.builder()
                .nickName(savedReview.getAccount().getNickName())
                .reviewContent(savedReview.getReviewContent())
                .build();


        return responseReviewDto;
    }
}
