package sourse.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sourse.dto.request.AuthCreationRequest;
import sourse.dto.request.IntrospectRequest;
import sourse.dto.response.AuthResponse;
import sourse.dto.response.IntrospectResponse;
import sourse.entity.Role;
import sourse.entity.User;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.repository.UserRepository;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {


    private final RedisTemplate<String, Object> redisTemplate;
    @NonFinal
//    doc bien tu file yaml
    @Value ("${jwt.signerKey}")
    protected String SECRET;
        UserRepository userRepository;
      public   AuthResponse authenticate(AuthCreationRequest request) {
          var user = userRepository.findByEmail(request.getEmail()).
                  orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
          PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
          if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
              throw new AppException(ErrorCode.LOGIN_FAILED);
          }
          var token = generateToken(user);
          boolean isFirstLogin = false;
          String redisKey = "user:first_login:" + user.getId();

          if (Boolean.FALSE.equals(redisTemplate.hasKey(redisKey))) {
              isFirstLogin = true;
              redisTemplate.opsForValue().set(redisKey, "true");
          }
          return AuthResponse.builder()
                  .firstName(user.getFirstName())
                  .lastName(user.getLastName())
                  .email(user.getEmail())
                  .idUser(user.getId())
                  .idRoom(user.getRoom() != null ? user.getRoom().getId().toString() : null)
                  .roomName(user.getRoom() != null ? user.getRoom().getName() : null)
                  .role(user.getRoles() != null
                          ? user.getRoles().stream()
                          .map(Role::getName)  // Lấy name của Role
                          .collect(Collectors.joining(", "))
                          : null)
                  .status(true)
                  .token(token)
                  .isFirstLogin(isFirstLogin)
                  .build();
      }
          public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
          var token = request.getToken();
          JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
          SignedJWT signedJWT = SignedJWT.parse(token);
          Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
          var verified = signedJWT.verify(verifier);
          return IntrospectResponse.builder().valid(  verified && expiryTime.after(new Date())).build();
      }
     
      private String generateToken(User user) {
//          header
      JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
//      payload
      JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(user.getEmail()).
              issuer("minhaudev.com").issueTime(new Date())
              .expirationTime(new Date(System.currentTimeMillis() + 360000 * 1000))
              .claim("scope", buildScope(user))
              .build();
          // Tạo JWT
          SignedJWT signedJWT = new SignedJWT(
                  header,
                  jwtClaimsSet
          );
          try {
              signedJWT.sign(new MACSigner(SECRET.getBytes()));
              return signedJWT.serialize();
          } catch (JOSEException e) {
              throw new RuntimeException(e);
          }
      }
      private String  buildScope (User user){

        StringJoiner stringJoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_"+role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                });
            });
        }
        return stringJoiner.toString();
      };
    public void checkLandlordPermission(String idOwner) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var email = authentication.getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        if (user.getRoles().stream().anyMatch(role -> "SUPERUSER".equals(role.getName()))) {
            return;
        }
        boolean isOwner = user.getId().equals(idOwner) &&  (user.getRoles().stream().anyMatch(role -> "LANDLORD".equals(role.getName())));
        if (!isOwner) {
            throw new AppException(ErrorCode.UNCATEGORIZED);
        }
    }

}   
