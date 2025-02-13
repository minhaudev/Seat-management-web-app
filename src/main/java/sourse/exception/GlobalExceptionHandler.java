package sourse.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sourse.core.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler (value = Exception.class)
    ResponseEntity<ApiResponse> handlingException(Exception e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED.getMessage());
    return ResponseEntity.badRequest().body(apiResponse);}

    @ExceptionHandler (value =  MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        String enumKey= e.getFieldError().getDefaultMessage();
       try {
            errorCode = ErrorCode.valueOf(enumKey);
       }   catch (IllegalArgumentException e1) {}


        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler (value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);}
}
