package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Room;
import sourse.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, String> {
    boolean existsByName(String name);
}
