package sourse.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatResponse {
    String id;
    String name;
    String userId;
    String roomId;
    String seatType;
    String status;
    String number;
    String description;
    String created;
}
