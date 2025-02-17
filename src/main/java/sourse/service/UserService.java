   package sourse.service;
   import lombok.AccessLevel;
   import lombok.RequiredArgsConstructor;
   import lombok.experimental.FieldDefaults;
   import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
   import org.springframework.security.crypto.factory.PasswordEncoderFactories;
   import org.springframework.security.crypto.password.PasswordEncoder;
   import org.springframework.stereotype.Service;
   import sourse.dto.request.UserUpdateRequest;
   import sourse.dto.response.UserResponse;
   import sourse.entity.User;
   import sourse.enums.EnumType;
   import sourse.exception.AppException;
   import sourse.exception.ErrorCode;
   import sourse.mapper.UserMapper;
   import sourse.repository.UserRepository;
   import sourse.dto.request.UserCreationRequest;

   import java.util.HashSet;

   @Service
   @RequiredArgsConstructor
   @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
   public class UserService {
      
       UserRepository userRepository;
       UserMapper userMapper;
       PasswordEncoder passwordEncoder;
      public UserResponse store(UserCreationRequest request) {
if(userRepository.existsByEmail(request.getEmail()))
   throw new AppException(ErrorCode.EMAIL_EXITED);
if(!request.isPasswordConfirmed()) {
   throw new AppException(ErrorCode.MATCH_PASSWORD);
}

          User user =  userMapper.toUser(request);
          user.setPassword(passwordEncoder.encode(request.getPassword()));
          HashSet<String> roles = new HashSet<>();
          roles.add(EnumType.UserRole.USER.name());
          user.setRoles(roles);
         userRepository.save(user);
           return userMapper.toUserResponse(user);
      }
      public User findById(String id) {
         return userRepository.findById(id)
                 .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
      }
      public UserResponse update( String id, UserUpdateRequest request) {
         User user = this.findById(id);
         userMapper.updateUser(user, request);
         return userMapper.toUserResponse(userRepository.save(user));

      }
      public void  delete(String id) {
         User user = this.findById(id);
         userRepository.delete(user);
          
      }
   public UserResponse show(String id) {
         User user = this.findById(id);
   return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found")));}
   }
