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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sourse.dto.request.AuthCreationRequest;
import sourse.dto.request.IntrospectRequest;
import sourse.dto.response.AuthResponse;
import sourse.dto.response.IntrospectResponse;
import sourse.entity.User;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.repository.UserRepository;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
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
                
              return AuthResponse.builder()
                      .firstName(user.getFirstName())
                      .lastName(user.getLastName())
                      .email(user.getEmail())
                      .idUser(user.getId())
                      .status(true)
                      .token(token)
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
              .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))
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
//        user.getRoles().forEach(stringJoiner::add);
        return stringJoiner.toString();
      };

}
