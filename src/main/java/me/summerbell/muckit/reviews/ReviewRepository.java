package me.summerbell.muckit.reviews;

import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByAccount(Account account);
}
