package me.summerbell.muckit.reviews;

import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.domain.Restaurant;
import me.summerbell.muckit.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByAccountAndRestaurant(Account account, Restaurant restaurant);

    @Query(value = "select r from Review r join fetch r.account where r.restaurant =:findRestaurant",
    countQuery = "select count(r) from Review r")
    Page<Review> findByRestaurant(Restaurant findRestaurant, Pageable pageable);
}
