package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Room;

public interface RoomRepository  extends JpaRepository<Room, String> {
    boolean existsByName(String name);
}
