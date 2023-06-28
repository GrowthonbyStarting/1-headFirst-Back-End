package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Badge;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Badge findByName(String name);
}
