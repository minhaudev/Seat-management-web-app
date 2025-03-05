package sourse.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatCreationRequest {
    @NotBlank
    String name;
    String number;
    String description;
    String typeSeat;
    String status;
//    String userId;
    String roomId;
}
