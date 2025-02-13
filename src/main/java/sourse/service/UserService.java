   package sourse.service;
   import lombok.AccessLevel;
   import lombok.RequiredArgsConstructor;
   import lombok.experimental.FieldDefaults;
   import org.springframework.stereotype.Service;
   import sourse.dto.request.UserUpdateRequest;
   import sourse.dto.response.UserResponse;
   import sourse.entity.User;
   import sourse.exception.AppException;
   import sourse.exception.ErrorCode;
   import sourse.mapper.UserMapper;
   import sourse.repository.UserRepository;
   import sourse.dto.request.UserCreationRequest;

   @Service
   @RequiredArgsConstructor
   @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
   public class UserService {
      
       UserRepository userRepository;
       UserMapper userMapper;
       
      public UserResponse store(UserCreationRequest request) {
if(userRepository.existsByEmail(request.getEmail()))
   throw new AppException(ErrorCode.EMAIL_EXITED);
          User user = userMapper.toUser(request);
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
