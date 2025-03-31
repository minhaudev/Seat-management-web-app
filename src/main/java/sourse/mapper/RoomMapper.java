package sourse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.request.UserUpdateRequest;
import sourse.dto.response.RoomResponse;
import sourse.entity.Room;
import sourse.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toRoom(RoomCreationRequest room);

    @Mapping(target = "owner", source = "user.id")
    @Mapping(target = "hallId", source = "hall.id")
    @Mapping(target = "object", source = "object", qualifiedByName = "mapObjectData")

    RoomResponse toRoomResponse(Room room);
    @Named("mapObjectData")
    static List<Room.ObjectData> mapObjectData(List<Room.ObjectData> objectData) {
        return objectData != null ? objectData : List.of(); // ✅ Trả về danh sách thay vì null
    }
    @Mapping(target = "id", ignore = true)
    void updateRoom(@MappingTarget Room room, RoomUpdateRequest request);
    List <RoomResponse>  toRoomResponseList (List <Room> rooms);
}
