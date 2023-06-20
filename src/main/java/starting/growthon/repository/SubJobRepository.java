package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.SubJob;

import java.util.List;

public interface SubJobRepository extends JpaRepository<SubJob, Long> {
    List<SubJob> findAllByNameContaining(String name);
}
