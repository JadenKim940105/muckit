package me.summerbell.muckit.likeit;

import me.summerbell.muckit.domain.LikeIt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeItRepository extends JpaRepository<LikeIt, Long> {

}
