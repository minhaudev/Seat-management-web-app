package sourse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.request.UserUpdateRequest;
import sourse.dto.response.RoomResponse;
import sourse.entity.Room;
import sourse.entity.User;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toRoom(RoomCreationRequest room);

    @Mapping(target = "owner", source = "user.id")
    @Mapping(target = "hallId", source = "hall.id")
    RoomResponse toRoomResponse(Room room);
    @Mapping(target = "id", ignore = true)
    void updateRoom(@MappingTarget Room room, RoomUpdateRequest request);
}
