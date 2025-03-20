package sourse.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sourse.entity.Room;
import sourse.entity.Seat;
import sourse.entity.User;
import sourse.enums.EnumType;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, String> {
    boolean existsByName(String name);

    boolean existsByUser(User user);
    Page<Seat> findByRoomId(String roomId, Pageable pageable);
    Page<Seat> findByRoomIdAndStatus(String roomId, EnumType.SeatStatus status, Pageable pageable);
    Page<Seat> findByStatus(EnumType.SeatStatus status, Pageable pageable);

    @Query("SELECT COALESCE(MAX(CAST(s.number AS int)), 0) FROM Seat s")
    int findMaxNumber();

    @Query("SELECT s.user.id FROM Seat s WHERE s.room.id = :roomId")
    List<String> findUserIdsByRoomId(@Param("roomId") String roomId);

}
