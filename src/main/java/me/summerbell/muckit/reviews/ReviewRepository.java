package me.summerbell.muckit.reviews;

import me.summerbell.muckit.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
