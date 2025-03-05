package sourse.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.entity.Role;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatUserResponse {
    String id;
    String name;
    String userId;
    String roomId;
    String typeSeat;
    String status;
    String number;
    String firstName;
    String lastName;
    String project;
    String team;
    String created;
}
