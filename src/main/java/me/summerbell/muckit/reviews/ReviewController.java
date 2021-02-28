package me.summerbell.muckit.reviews;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.accounts.auth.CurrentUser;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.domain.Restaurant;
import me.summerbell.muckit.domain.Review;
import me.summerbell.muckit.kakaosearchapi.dto.RestaurantDto;
import me.summerbell.muckit.utils.vo.RestaurantReviewVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @ExceptionHandler
    public ResponseEntity reviewErrorHandler(NoSuchElementException exception){
        return ResponseEntity.badRequest().body("등록된 리뷰가 없습니다.");
    }

    @GetMapping("/api/restaurant-review")
    public ResponseEntity getRestaurantReview(@RequestBody RestaurantDto restaurantDto){

        String kakaoId = restaurantDto.getKakao_id();

        List<Review> review = reviewService.getReview(kakaoId);

        return ResponseEntity.ok(review);
    }

    // 최초 리뷰 생성의 경우
    @PostMapping("/api/restaurant-review")
    public ResponseEntity createRestaurantReview(@RequestBody RestaurantReviewVo vo, @CurrentUser Account account){

        // 1. 해당 레스토랑 데이터를 DB에 저장한다.
        Restaurant savedRestaurant = reviewService.saveRestaurantInfo(vo);

        // 2. 리뷰를 생성해 추가한다.
        ReviewDto review = reviewService.createReview(savedRestaurant, vo, account);

        return ResponseEntity.ok(review);
    }

    // todo 리뷰 추가하기 ( 이미 최초 리뷰가 있는 경우 )

}
