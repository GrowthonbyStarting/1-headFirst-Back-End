package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.MentorInfo;

import java.util.Optional;

public interface MentorInfoRepository extends JpaRepository<MentorInfo, Long> {
    Optional<MentorInfo> findByMentorId(Long id);
}
