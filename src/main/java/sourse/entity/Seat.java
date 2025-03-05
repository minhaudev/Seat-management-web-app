package sourse.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.core.BaseEntity;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "seats", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Seat extends BaseEntity {
    @Column(unique = true, nullable = false)
    String name;
    String number;
    String description;

    @Enumerated(EnumType.STRING)
    sourse.enums.EnumType.TypeSeat typeSeat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    sourse.enums.EnumType.SeatStatus status;

    @ManyToOne
    @JoinColumn(name="room_id", referencedColumnName = "id")
    Room room;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    User user;
}
