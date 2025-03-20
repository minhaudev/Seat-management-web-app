package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Hall;
import sourse.entity.Room;

import java.util.List;

public interface RoomRepository  extends JpaRepository<Room, String> {
    boolean existsByName(String name);
    List<Room> findByHallId(String hallId);
}
