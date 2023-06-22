package starting.growthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starting.growthon.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
    File findByTypeAndOwnerId(String type, Long ownerId);
}
