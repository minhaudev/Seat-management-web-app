package sourse.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class AuthCreationRequest {
    @NotBlank
    @Email(message = "FORMAT_EMAIL")
    String email;
    @NotBlank
    @Size(min= 8, message = "PASSWORD")
    String password;
}
