package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByDayAndTime(String day, String time);
}
