package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.MentorAndSchedule;

import java.util.List;

public interface MentorAndScheduleRepository extends JpaRepository<MentorAndSchedule, Long> {
    List<MentorAndSchedule> findAllByMentorId(Long mentorId);
}
