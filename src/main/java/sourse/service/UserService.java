       package sourse.service;
       import lombok.AccessLevel;
       import lombok.RequiredArgsConstructor;
       import lombok.experimental.FieldDefaults;
       import lombok.extern.slf4j.Slf4j;
       import org.slf4j.ILoggerFactory;
       import org.springframework.context.ApplicationContext;
       import org.springframework.context.annotation.Lazy;
       import org.springframework.data.redis.core.HashOperations;
       import org.springframework.data.redis.core.RedisTemplate;
       import org.springframework.data.redis.core.StringRedisTemplate;
       import org.springframework.data.redis.core.ValueOperations;
       import org.springframework.security.access.prepost.PostAuthorize;
       import org.springframework.security.access.prepost.PreAuthorize;
       import org.springframework.security.core.context.SecurityContextHolder;
       import org.springframework.security.crypto.password.PasswordEncoder;
       import org.springframework.security.oauth2.jwt.Jwt;
       import org.springframework.stereotype.Service;
       import org.springframework.transaction.annotation.Transactional;
       import sourse.dto.request.UserUpdateRequest;
       import sourse.dto.response.UserInRoomResponse;
       import sourse.dto.response.UserResponse;
       import sourse.entity.*;
       import sourse.enums.EnumType;
       import sourse.exception.AppException;
       import sourse.exception.ErrorCode;
       import sourse.mapper.UserMapper;
       import sourse.repository.*;
       import sourse.dto.request.UserCreationRequest;

       import java.util.HashSet;
       import java.util.List;
       import java.util.Optional;
       import java.util.Set;
       import java.util.concurrent.TimeUnit;
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
           private static final String TEAM_COLOR_HASH = "team_colors";
           private static final String PROJECT_COLOR_HASH = "project_colors";
           private final ApplicationContext applicationContext;
           private final SeatRepository seatRepository;
           private final StringRedisTemplate redisTemplate;
           private final ProjectRepository projectRepository;
           private final TeamRepository teamRepository;

           public User findById(String id) {
               return userRepository.findById(id)
                       .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
           }
           private RoomService getRoomService() {
               return applicationContext.getBean(RoomService.class);
           }

           private String generateNewColor() {
               return "#" + String.format("%06X", (int) (Math.random() * 0xFFFFFF));
           }
           private String resolveColorFromTeamOrProject(String teamId, String projectId) {
               HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

               if (teamId != null) {
                   String color = hashOps.get(TEAM_COLOR_HASH, teamId);

                   if (color == null) {
                       Optional<User> existingUser = userRepository.findFirstByTeam_Id(teamId);
                       color = existingUser.map(User::getColor).orElseGet(this::generateNewColor);
                       hashOps.put(TEAM_COLOR_HASH, teamId, color);
                   }

                   return color;
               }

               if (projectId != null) {
                   String color = hashOps.get(PROJECT_COLOR_HASH, projectId);

                   if (color == null) {
                       Optional<User> existingUser = userRepository.findFirstByProject_Id(projectId);
                       color = existingUser.map(User::getColor).orElseGet(this::generateNewColor);
                       hashOps.put(PROJECT_COLOR_HASH, projectId, color);
                   }

                   return color;
               }

               return "#FFFFFF";
           }


           public UserResponse store(UserCreationRequest request) {
               if (userRepository.existsByEmail(request.getEmail())) {
                   throw new AppException(ErrorCode.EMAIL_EXITED);
               }

               if (!request.isPasswordConfirmed()) {
                   throw new AppException(ErrorCode.MATCH_PASSWORD);
               }

               User user = userMapper.toUser(request);
               user.setPassword(passwordEncoder.encode(request.getPassword()));

               // Gán ROLE mặc định
               Role defaultRole = roleRepository.findByName("USER")
                       .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
               user.setRoles(Set.of(defaultRole));

               HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
               String color = "#FFFFFF";

               // Gán team nếu có
               if (request.getTeamId() != null) {
                   Team team = teamRepository.findById(request.getTeamId())
                           .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
                   user.setTeam(team);

                   String teamId = team.getId();
                   String colorValue = hashOps.get(TEAM_COLOR_HASH, teamId);

                   if (colorValue == null) {
                       // Lưu tên và màu vào Redis
                       colorValue = team.getName() + "," + generateNewColor();
                       hashOps.put(TEAM_COLOR_HASH, teamId, colorValue);
                   }

                   // Chỉ lấy màu
                   color = colorValue.split(",")[1]; // Lấy màu
               }

               // Gán project nếu có
               if (request.getProjectId() != null) {
                   Project project = projectRepository.findById(request.getProjectId())
                           .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
                   user.setProject(project);

                   String projectId = project.getId();
                   String colorValue = hashOps.get(PROJECT_COLOR_HASH, projectId);

                   // Chỉ override nếu chưa có color từ team
                   if (color.equals("#FFFFFF")) {
                       if (colorValue == null) {
                           // Lưu tên và màu vào Redis
                           colorValue = project.getName() + "," + generateNewColor();
                           hashOps.put(PROJECT_COLOR_HASH, projectId, colorValue);
                       }


                       color = colorValue.split(",")[1];
                   }
               }


               user.setColor(color);
               userRepository.save(user);

               return userMapper.toUserResponse(user);
           }

           public UserResponse update(String id, UserUpdateRequest request) {

               User user = this.findById(id);
               System.out.println("user: " + user.getEmail() + request.getEmail());
               if(!request.getEmail().equals(user.getEmail()) ) {
                   if (userRepository.existsByEmail(request.getEmail())) {
                       throw new AppException(ErrorCode.EMAIL_EXITED);
                   }
               }

               if (request.getRoomId() != null) {
                   Room room = getRoomService().findById(request.getRoomId());
                   user.setRoom(room);
               }

               if (request.getTeamId() != null) {
                   Team team = teamRepository.findById(request.getTeamId())
                           .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
                   user.setTeam(team);
               }

               if (request.getProjectId() != null) {
                   Project project = projectRepository.findById(request.getProjectId())
                           .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
                   user.setProject(project);
               }

               if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                   var roles = roleRepository.findAllById(request.getRoles());
                   user.setRoles(new HashSet<>(roles));
               }

               if (request.getTeamId() != null || request.getProjectId() != null) {
                   HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
                   String color = "#FFFFFF";

                   if (request.getTeamId() != null) {
                       String teamId = request.getTeamId();
                       String colorValue = hashOps.get(TEAM_COLOR_HASH, teamId);

                       if (colorValue == null) {
                           Team team = teamRepository.findById(teamId)
                                   .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
                           colorValue = team.getName() + "," + generateNewColor();
                           hashOps.put(TEAM_COLOR_HASH, teamId, colorValue);
                       }


                       color = colorValue.split(",")[1];
                   }

                   if (request.getProjectId() != null && color.equals("#FFFFFF")) {
                       String projectId = request.getProjectId();
                       String colorValue = hashOps.get(PROJECT_COLOR_HASH, projectId);

                       if (colorValue == null) {
                           Project project = projectRepository.findById(projectId)
                                   .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
                           colorValue = project.getName() + "," + generateNewColor();
                           hashOps.put(PROJECT_COLOR_HASH, projectId, colorValue);
                       }

                       // Chỉ lấy màu
                       color = colorValue.split(",")[1]; // Lấy màu
                   }

                   user.setColor(color);
               }
               userMapper.updateUser(user, request);
               return userMapper.toUserResponse(userRepository.save(user));
           }
           @Transactional
           @PreAuthorize("hasRole('SUPERUSER')")
           public void delete(String id) {
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
//           @PreAuthorize("hasRole('SUPERUSER')")
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

