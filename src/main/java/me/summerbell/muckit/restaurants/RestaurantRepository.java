package me.summerbell.muckit.restaurants;

import me.summerbell.muckit.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByKakaoId(String restaurantKakaoId);

}
