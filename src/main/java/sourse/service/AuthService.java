package sourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sourse.dto.request.EmailRequest;
import sourse.dto.request.ResetPasswordRequest;
import sourse.entity.User;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.repository.UserRepository;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    EmailService emailService;
    RedisTemplate<String, String> redisTemplate;
    PasswordEncoder passwordEncoder;
    public ResponseEntity<?> sendResetToken(EmailRequest request) throws AppException {
    Optional<User> user = userRepository.findByEmail(request.getEmail());
    System.out.println("user, email" + user.get().getEmail() + request.getEmail());
    if(user.isEmpty()){
        throw new AppException(ErrorCode.EMAIL_NOT_FOUND);
    }
    String token = UUID.randomUUID().toString();
    redisTemplate.opsForValue().set("reset_token:"+ token, request.getEmail(), Duration.ofMinutes(15));
        emailService.sendResetPasswordEmail(request.getEmail(), token);
        return ResponseEntity.ok("Sent Reset password email to " + request.getEmail());
    }
    public ResponseEntity<?> resetPassword(ResetPasswordRequest request) {

    String email = redisTemplate.opsForValue().get("reset_token:"+ request.getToken());
     if(email == null){
        throw  new AppException(ErrorCode.TOKEN_INVALID);
     }
     User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
     user.setPassword(passwordEncoder.encode(request.getNewPassword()));
     userRepository.save(user);
     redisTemplate.delete("reset_token:" + request.getToken());
        return ResponseEntity.ok("Password reset successfully!");
    }
}
