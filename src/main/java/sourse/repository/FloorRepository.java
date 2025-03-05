package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Floor;
import sourse.entity.Room;

public interface FloorRepository extends JpaRepository<Floor, String> {
    boolean existsByName(String floor);
}
