package me.summerbell.muckit.reviews;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.domain.Restaurant;
import me.summerbell.muckit.domain.Review;
import me.summerbell.muckit.restaurants.RestaurantRepository;
import me.summerbell.muckit.utils.vo.RestaurantReviewVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final RestaurantRepository restaurantRepository;

    private final StorageService s3StorageService;

    public Page<ReviewDto> getReview(String restaurantKakaoId, Pageable pageable) {
        Optional<Restaurant> findRestaurant = restaurantRepository.findByKakaoId(restaurantKakaoId);
        findRestaurant.orElseThrow(NoSuchElementException::new);

        Page<Review> reviews = reviewRepository.findByRestaurant(findRestaurant.get(), pageable);

        return reviews.map(r -> new ReviewDto(r.getAccount().getNickName(), r.getReviewContent(), r.getImageUrl()));
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

    public ReviewDto createReview(RestaurantReviewVo vo, Optional<MultipartFile> uploadImage, Account account) {
        // 1. 최초 리뷰인지 아닌지 확인
        String kakaoId = vo.getKakao_id();
        Optional<Restaurant> restaurant = restaurantRepository.findByKakaoId(kakaoId);
        if(restaurant.isEmpty()){      // 저장된 레스토랑 정보가 없음 == 최초 리뷰.
            Restaurant newRestaurant = saveRestaurantInfo(vo);  // 레스토랑 DB 에 저장
            return createReview(newRestaurant, vo, uploadImage, account);
        } else {
            return createReview(restaurant.get(), vo, uploadImage, account);
        }
    }

    private ReviewDto createReview(Restaurant restaurant, RestaurantReviewVo vo, Optional<MultipartFile> uploadImage, Account account) {
        Optional<Review> alreadyWrittenReview = reviewRepository.findByAccountAndRestaurant(account, restaurant);


        if(alreadyWrittenReview.isPresent()){
            throw new AlreadyWrittenException("리뷰를 중복해서 남기실 수 없습니다.");
        }


        String imageUrl = null;
        if(uploadImage.isPresent()){
            imageUrl = s3StorageService.uploadFile(uploadImage.get());
        }

        Review newReview = Review.builder()
                .account(account)
                .reviewContent(vo.getReviewContent())
                .restaurant(restaurant)
                .createdAt(LocalDateTime.now())
                .imageUrl(imageUrl)
                .build();

        Review savedReview = reviewRepository.save(newReview);


        return ReviewDto.builder()
                .nickName(savedReview.getAccount().getNickName())
                .reviewContent(savedReview.getReviewContent())
                .imageUrl(savedReview.getImageUrl())
                .build();
    }
}
