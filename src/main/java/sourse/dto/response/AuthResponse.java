package sourse.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String idUser;
    String idRoom;
    String roomName;
    String role;
    String firstName;
    String lastName;
    String email;
    boolean status;
     String token;
    boolean isFirstLogin;
}