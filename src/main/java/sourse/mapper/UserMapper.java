package sourse.mapper;


import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import sourse.dto.request.UserCreationRequest;
import sourse.dto.request.UserUpdateRequest;
import sourse.dto.response.UserInRoomResponse;
import sourse.dto.response.UserResponse;
import sourse.entity.Project;
import sourse.entity.Team;
import sourse.entity.User;
import sourse.repository.ProjectRepository;
import sourse.repository.TeamRepository;
import sourse.service.ColorService;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);
    @Mapping(target = "color", expression = "java(colorService.getColor(user.getTeam() != null ? user.getTeam().getName() : null, user.getProject() != null ? user.getProject().getName() : null))")
    UserResponse toUserResponseRedis(User user, @Context ColorService colorService);

    @Mapping(target = "project", expression = "java(mapProjectName(user.getProject()))")
    @Mapping(target = "team", expression = "java(mapTeamName(user.getTeam()))")
    @Mapping(target = "roomId", source = "room.id")
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);
    List<UserInRoomResponse> toUserInRoomResponseList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    default String mapProjectName(Project project) {
        return project != null ? project.getName() : null;
    }

    default String mapTeamName(Team team) {
        return team != null ? team.getName() : null;
    }
}





