package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Follow;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByMentorIdAndMenteeId(Long mentorId, Long menteeId);
    void deleteByMentorIdAndMenteeId(Long mentorId, Long menteeId);
    List<Follow> findAllByMentorId(Long mentorId);
}
