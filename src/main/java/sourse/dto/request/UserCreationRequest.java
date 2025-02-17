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
public class UserCreationRequest {
    @Email(message = "FORMAT_EMAIL")
    @NotBlank
    String email;
    @Size(min =2, message = "FirstName must be at least 2 characters.")
    String firstName;
    @Size(min =3, message = "LastName must be at least 3 characters.")
    String lastName;
    @Size(min=2, max = 15, message =  "PHONE_NUMBER")
    String phone;
    @Size(min= 8, message = "PASSWORD")
    @NotBlank
    String password;
    @NotBlank
    String confirmPassword;
    String role;
    String project;
    String team;

    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}
