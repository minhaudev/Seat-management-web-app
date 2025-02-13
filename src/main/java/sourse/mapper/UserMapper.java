package sourse.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sourse.dto.request.UserCreationRequest;
import sourse.dto.request.UserUpdateRequest;
import sourse.dto.response.UserResponse;
import sourse.entity.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}

