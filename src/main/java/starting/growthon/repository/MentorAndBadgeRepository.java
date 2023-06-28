package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.MentorAndBadge;

import java.util.List;

public interface MentorAndBadgeRepository extends JpaRepository<MentorAndBadge, Long> {
    List<MentorAndBadge> findAllByMentorId(Long mentorId);
}
