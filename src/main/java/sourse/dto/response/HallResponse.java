package sourse.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HallResponse {
    String id;
    String name;
    String description;
    String floorId;
    String created;
}
