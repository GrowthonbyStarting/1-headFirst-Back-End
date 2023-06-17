package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
