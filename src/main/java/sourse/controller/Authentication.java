package sourse.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import sourse.core.ApiResponse;
import sourse.dto.request.AuthCreationRequest;
import sourse.dto.request.EmailRequest;
import sourse.dto.request.IntrospectRequest;
import sourse.dto.request.ResetPasswordRequest;
import sourse.dto.response.AuthResponse;
import sourse.dto.response.IntrospectResponse;
import sourse.service.AuthService;
import sourse.service.AuthenticationService;

import java.text.ParseException;


@RestController
@RequestMapping("api/auth")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Authentication {
    AuthenticationService authenticationService;
    private final RestClient.Builder builder;
    private final AuthService authService;

    @PostMapping("/login")
   public ApiResponse <AuthResponse>  login (@RequestBody @Valid AuthCreationRequest request){
        AuthResponse authResponse = authenticationService.authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect (@RequestBody IntrospectRequest token) throws ParseException, JOSEException {
       var response = authenticationService.introspect(token);
        return ApiResponse.<IntrospectResponse>builder().data(response).build();

    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestResetPassword(@RequestBody EmailRequest request) {
        System.out.print("email"+ request.getEmail() );
        return authService.sendResetToken(request);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }
}
