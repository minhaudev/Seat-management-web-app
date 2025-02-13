package sourse.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.core.BaseEntity;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Floor extends BaseEntity {
     String name;
     String description;
}
