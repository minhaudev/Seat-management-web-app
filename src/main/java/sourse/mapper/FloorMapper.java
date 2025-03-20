package sourse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sourse.dto.request.FloorCreationRequest;
import sourse.dto.request.FloorUpdateRequest;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.response.FloorResponse;
import sourse.dto.response.RoomResponse;
import sourse.dto.response.UserResponse;
import sourse.entity.Floor;
import sourse.entity.Room;
import sourse.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FloorMapper {
    Floor toFloor(FloorCreationRequest floor);
    FloorResponse toFloorResponse(Floor floor);
    @Mapping(target = "id", ignore = true)
    void updateFloor(@MappingTarget Floor floor, FloorUpdateRequest request);
    List<FloorResponse> toFloorResponseList(List<Floor> floors);

}
