package sourse.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    EMAIL_EXITED(1001, "Email exited!", HttpStatus.BAD_REQUEST),
    PASSWORD(1002, "Password least 8 chars!",  HttpStatus.BAD_REQUEST),
    FORMAT_EMAIL(1003, "email is not in correct format",  HttpStatus.BAD_REQUEST),
    PHONE_NUMBER(1004, "Phone number must not exceed 15 characters.", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1005, "Invalid key!",  HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1006, "User not found!", HttpStatus.NOT_FOUND),
    ROOM_NOT_FOUND(1007, "Room not found!",HttpStatus.NOT_FOUND),
    SEAT_NOT_FOUND(1008, "Seat not found!",HttpStatus.NOT_FOUND),
    FLOOR_NOT_FOUND(1009, "Floor not found!",HttpStatus.NOT_FOUND),
    HALL_NOT_FOUND(1010, "Hall not found!",HttpStatus.NOT_FOUND),
    NAME_EXITED(1011, "Name exited!", HttpStatus.BAD_REQUEST),
    MATCH_PASSWORD(1012, "Password is not match!", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED(1015, "Email or password is incorrect!", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED(1013, "You don't have permission to access!", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1014, "User is not authenticated!",HttpStatus.UNAUTHORIZED),
    PERMISSION_NOT_FOUND(1016, "Permission not found!", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1017, "Role not found!", HttpStatus.BAD_REQUEST),
    ROLE_ALREADY_EXISTS(1018, "Role exists", HttpStatus.BAD_REQUEST),
    PERMISSION_ALREADY_EXISTS(1019, "Permission exists", HttpStatus.BAD_REQUEST),
    SEAT_ALREADY_ASSIGNED(1020,"This chair is occupied", HttpStatus.BAD_REQUEST),
    NO_USER_IN_SEAT(1021, "There is no one sitting in this chair!", HttpStatus.BAD_REQUEST),
    SEAT_NOT_CHANGE(1022, "This seat cannot be changed!", HttpStatus.BAD_REQUEST),
    SEAT_TAKEN(1023, "This seat is already taken!", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized-sever!",HttpStatus.INTERNAL_SERVER_ERROR);
    int code;
    String message;
    HttpStatusCode statusCode;
}
