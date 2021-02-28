package me.summerbell.muckit.domain;

import me.summerbell.muckit.restaurants.RestaurantRepository;
import me.summerbell.muckit.reviews.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RestaurantTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void restaurant_reviewTest(){
        Restaurant restaurant = new Restaurant();
        restaurant.setPlaceName("레스토랑1");

        Review review = new Review();
        review.setReviewContent("리뷰1");
        review.setRestaurant(restaurant);

        Review review2 = new Review();
        review2.setReviewContent("리뷰2");
        review2.setRestaurant(restaurant);

        restaurant.addReview(review);
        restaurant.addReview(review2);

        restaurantRepository.save(restaurant);

        Restaurant savedRestaurant = restaurantRepository.findAll().get(0);

        savedRestaurant.getReviewList().forEach(r -> {
            assertThat(r.getId()).isNotNull();
        });

        assertThat(savedRestaurant.getReviewList()).size().isEqualTo(2);
        assertThat(savedRestaurant.getReviewList().get(0).getReviewContent()).isEqualTo(review.getReviewContent());

    }

}