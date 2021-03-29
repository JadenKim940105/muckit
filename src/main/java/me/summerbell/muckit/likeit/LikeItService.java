package me.summerbell.muckit.likeit;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.domain.LikeIt;
import me.summerbell.muckit.domain.Restaurant;
import me.summerbell.muckit.kakaosearchapi.dto.RestaurantDto;
import me.summerbell.muckit.restaurants.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeItService {

    private final LikeItRepository likeItRepository;
    private final RestaurantRepository restaurantRepository;


    public LikeIt addLikeIt(Account account, RestaurantDto restaurantDto) {
        Optional<Restaurant> findRestaurant = restaurantRepository.findByKakaoId(restaurantDto.getKakao_id());

        LikeIt likeIt;

        if(findRestaurant.isEmpty()){
            Restaurant restaurant = createRestaurant(restaurantDto);
             likeIt = LikeIt.builder()
                    .restaurant(restaurant)
                    .account(account)
                    .build();
            return likeItRepository.save(likeIt);
        } else {
            likeIt = LikeIt.builder()
                    .restaurant(findRestaurant.get())
                    .account(account)
                    .build();
            return likeIt;
        }
    }

    private Restaurant createRestaurant(RestaurantDto restaurantDto) {
        Restaurant newRestaurant = Restaurant.builder()
                .addressName(restaurantDto.getAddress_name())
                .kakaoId(restaurantDto.getKakao_id())
                .phone(restaurantDto.getPhone())
                .placeName(restaurantDto.getPlace_name())
                .placeUrl(restaurantDto.getPlace_url())
                .roadAddressName(restaurantDto.getRoad_address_name())
                .longitude(restaurantDto.getLongitude())
                .latitude(restaurantDto.getLatitude())
                .build();

        return restaurantRepository.save(newRestaurant);
    }
}
