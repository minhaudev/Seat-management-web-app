package sourse.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
