package me.summerbell.muckit.reviews;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.summerbell.muckit.accounts.auth.CurrentUser;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.utils.vo.RestaurantReviewVo;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final ObjectMapper objectMapper;

    @ExceptionHandler
    public ResponseEntity reviewErrorHandler(NoSuchElementException exception){
        return ResponseEntity.badRequest().body("등록된 리뷰가 없습니다.");
    }

    @ExceptionHandler
    public ResponseEntity alreadyWrittenHandler(AlreadyWrittenException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @GetMapping("/api/restaurant-review")
    public ResponseEntity getRestaurantReview(@RequestParam String restaurantKakaoId){

        List<ReviewDto> review = reviewService.getReview(restaurantKakaoId);

        return ResponseEntity.ok(review);
    }

    @PostMapping(value = "/api/restaurant-review",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity createRestaurantReview(@RequestPart String reviewInfo,
                                                 @RequestParam Optional<MultipartFile> uploadImage,
                                                 @CurrentUser Account account){

        // ios 로 배열밖에 넘기지 못해, 값 변환
        reviewInfo = reviewInfoConvert(reviewInfo);


        RestaurantReviewVo restaurantReviewVo = new RestaurantReviewVo();
        try{
            restaurantReviewVo = objectMapper.readValue(reviewInfo, RestaurantReviewVo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ReviewDto review = reviewService.createReview(restaurantReviewVo, uploadImage, account);

        return ResponseEntity.ok(review);
    }

    private String reviewInfoConvert(String reviewInfo){
        reviewInfo = reviewInfo.replace("[", "{");
        reviewInfo = reviewInfo.replace("]","}");
        return reviewInfo;
    }



    // todo 리뷰 추가하기 ( 이미 최초 리뷰가 있는 경우 )

}
