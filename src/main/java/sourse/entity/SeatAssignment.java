package sourse.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.core.BaseEntity;

import java.time.LocalDateTime;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "seatassignment")
public class SeatAssignment extends BaseEntity {

    LocalDateTime assignedAt = LocalDateTime.now();
    LocalDateTime reassignedAt  = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="SeatId")
    Seat seat;
    
    @ManyToOne
    @JoinColumn(name="UserId")
    User user;
}
