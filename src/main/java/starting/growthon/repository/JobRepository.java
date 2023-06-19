package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
