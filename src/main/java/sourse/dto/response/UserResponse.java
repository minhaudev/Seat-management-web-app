package sourse.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.entity.Role;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String firstName;
    String lastName;
    String email;
    String phone;
    Set<Role> roles;
    String project;
    String team;
    String color;
    String created;
}
