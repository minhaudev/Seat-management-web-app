package sourse.entity;

import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.dto.response.SeatUserResponse;

import java.time.LocalDateTime;

@Data
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "SeatChanges")
public class SeatChangeData {
    String id;
    String name;
    String number;
    String description;
    String typeSeat;
    String status;
    String userId;
    SeatUserResponse user;
    LocalDateTime expiredAt;
    int ox;
    int oy;
}
