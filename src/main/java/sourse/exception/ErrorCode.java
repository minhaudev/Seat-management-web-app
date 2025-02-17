package sourse.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    EMAIL_EXITED(1001, "Email exited!" ),
    PASSWORD(1002, "Password least 8 chars!"),
    FORMAT_EMAIL(1003, "email is not in correct format"),
    PHONE_NUMBER(1004, "Phone number must not exceed 15 characters."),
    INVALID_KEY(1005, "Invalid key!"),
    USER_NOT_FOUND(1006, "User not found!"),
    ROOM_NOT_FOUND(1007, "Room not found!"),
    SEAT_NOT_FOUND(1008, "Seat not found!"),
    FLOOR_NOT_FOUND(1009, "Floor not found!"),
    HALL_NOT_FOUND(1010, "Hall not found!"),
    NAME_EXITED(1011, "Name exited!"),
    MATCH_PASSWORD(1012, "Password is not match!"),
    LOGIN_FAILED(1012, "Email or password is incorrect!"),
    UNCATEGORIZED(9999, "Uncategorized!");
    
    private int code;
    private String message;
}
