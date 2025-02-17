package sourse.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SeatResponse {
    String id;
    String name;
    String userId;
    String roomId;
    String typeSeat;
    String status;
    String number;
    String description;
    String created;
}
