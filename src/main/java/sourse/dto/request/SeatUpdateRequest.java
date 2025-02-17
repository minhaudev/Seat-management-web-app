package sourse.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatUpdateRequest {
    @NotBlank
    String name;
    String description;
    String number;
    String  seatType;
    String  status;
}
