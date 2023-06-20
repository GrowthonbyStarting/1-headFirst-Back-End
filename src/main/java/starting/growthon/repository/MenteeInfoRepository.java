package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.MenteeInfo;

public interface MenteeInfoRepository extends JpaRepository<MenteeInfo, Long> {
}
