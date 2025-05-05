package sourse.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.enums.EnumType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatAssignmentRequest {
    @NotNull
     EnumType.TypeSeat typeSeat;
    LocalDateTime expiredAt;
}
