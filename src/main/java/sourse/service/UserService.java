       package sourse.service;
       import lombok.AccessLevel;
       import lombok.RequiredArgsConstructor;
       import lombok.experimental.FieldDefaults;
       import lombok.extern.slf4j.Slf4j;
       import org.slf4j.ILoggerFactory;
       import org.springframework.context.ApplicationContext;
       import org.springframework.context.annotation.Lazy;
       import org.springframework.security.access.prepost.PostAuthorize;
       import org.springframework.security.access.prepost.PreAuthorize;
       import org.springframework.security.core.context.SecurityContextHolder;
       import org.springframework.security.crypto.password.PasswordEncoder;
       import org.springframework.security.oauth2.jwt.Jwt;
       import org.springframework.stereotype.Service;
       import sourse.dto.request.UserUpdateRequest;
       import sourse.dto.response.UserInRoomResponse;
       import sourse.dto.response.UserResponse;
       import sourse.entity.Role;
       import sourse.entity.Room;
       import sourse.entity.User;
       import sourse.enums.EnumType;
       import sourse.exception.AppException;
       import sourse.exception.ErrorCode;
       import sourse.mapper.UserMapper;
       import sourse.repository.RoleRepository;
       import sourse.repository.SeatRepository;
       import sourse.repository.UserRepository;
       import sourse.dto.request.UserCreationRequest;

       import java.util.HashSet;
       import java.util.List;
       import java.util.Set;
       import java.util.stream.Collectors;

       @Slf4j
       @Service
       @RequiredArgsConstructor
       @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
       public class UserService {
           UserRepository userRepository;
           UserMapper userMapper;
           PasswordEncoder passwordEncoder;
           RoleRepository roleRepository;
           ColorService colorService;

           private final ApplicationContext applicationContext;
           private final SeatRepository seatRepository;

           private RoomService getRoomService() {
               return applicationContext.getBean(RoomService.class);
           }

           public UserResponse store(UserCreationRequest request) {
    if(userRepository.existsByEmail(request.getEmail()))
       throw new AppException(ErrorCode.EMAIL_EXITED);
    if(!request.isPasswordConfirmed()) {
       throw new AppException(ErrorCode.MATCH_PASSWORD);
    }
              User user =  userMapper.toUser(request);
              user.setPassword(passwordEncoder.encode(request.getPassword()));
              Role defaultRole = roleRepository.findByName("USER")
                      .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
              user.setRoles(Set.of(defaultRole));
             userRepository.save(user);
               return userMapper.toUserResponseRedis(user,colorService);
          }
          public User findById(String id) {
             return userRepository.findById(id)
                     .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
          }

    //       @PreAuthorize("returnObject.email == authentication.name or hasRole('SUPERUSER')" )
    @PreAuthorize("hasRole('SUPERUSER')")
          public UserResponse update( String id, UserUpdateRequest request) {
             User user = this.findById(id);
        Room room = getRoomService().findById(request.getRoomId());
        user.setRoom(room);
             var roles = roleRepository.findAllById(request.getRoles());
             user.setRoles(new HashSet<>(roles));
             userMapper.updateUser(user, request);
             return userMapper.toUserResponse(userRepository.save(user));

          }
           @PreAuthorize("hasRole('SUPERUSER')")
          public void  delete(String id) {
             User user = this.findById(id);
             userRepository.delete(user);

          }
          @PostAuthorize("returnObject.email == authentication.name or hasRole('SUPERUSER')" )
       public UserResponse show(String id) {
               var authentication = SecurityContextHolder.getContext().getAuthentication();
             User user = this.findById(id);
       return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
          }
           @PreAuthorize("hasRole('SUPERUSER')")
           public List<UserResponse> index() {
              return userMapper.toUserResponseList(userRepository.findAll());
           }
           public UserResponse showInfo() {
                     var authentication = SecurityContextHolder.getContext().getAuthentication();
                     var email = authentication.getName();
                     return userMapper.toUserResponse(userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
           }
           @PreAuthorize("hasRole('SUPERUSER')")
           public List<UserInRoomResponse> userInRoom(String roomId) {
               Room room = getRoomService().findById(roomId);
               List<User> users = userRepository.findByRoomId(room.getId());
               List<String> usersWithSeat = seatRepository.findUserIdsByRoomId(roomId);

               List<User> usersWithoutSeat = users.stream()
                       .filter(user -> !usersWithSeat.contains(user.getId()))
                       .collect(Collectors.toList());
               return userMapper.toUserInRoomResponseList(usersWithoutSeat);
           }

       }

