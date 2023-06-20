package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.UserAndKeyword;

public interface UserAndKeywordRepository extends JpaRepository<UserAndKeyword, Long> {
}
