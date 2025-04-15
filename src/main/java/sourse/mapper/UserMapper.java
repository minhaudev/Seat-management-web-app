package sourse.mapper;


import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sourse.dto.request.UserCreationRequest;
import sourse.dto.request.UserUpdateRequest;
import sourse.dto.response.UserInRoomResponse;
import sourse.dto.response.UserResponse;
import sourse.entity.User;
import sourse.service.ColorService;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "color", expression = "java(colorService.getColor(user.getTeam(), user.getProject()))")
    UserResponse toUserResponseRedis(User user, @Context ColorService colorService);
    @Mapping(target = "roomId", source = "room.id")
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);
    List<UserInRoomResponse> toUserInRoomResponseList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);


}



