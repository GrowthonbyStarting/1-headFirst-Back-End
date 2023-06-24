package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.MentorInfo;

public interface MentorInfoRepository extends JpaRepository<MentorInfo, Long> {
    MentorInfo findByMentorId(Long id);
}
