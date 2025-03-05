package sourse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Room;
import sourse.entity.Seat;
import sourse.enums.EnumType;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, String> {
    boolean existsByName(String name);
    Page<Seat> findByRoomId(String roomId, Pageable pageable);
    Page<Seat> findByRoomIdAndStatus(String roomId, EnumType.SeatStatus status, Pageable pageable);
    Page<Seat> findByStatus(EnumType.SeatStatus status, Pageable pageable);
}
