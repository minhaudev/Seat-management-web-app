package sourse.mapper;

import org.mapstruct.*;

import sourse.dto.request.SeatCreationRequest;
import sourse.dto.request.SeatUpdateRequest;
import sourse.dto.response.SeatResponse;
import sourse.entity.Seat;
import sourse.enums.EnumType;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "typeSeat", source = "typeSeat")
    Seat toSeat(SeatCreationRequest seat);


    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "roomId", source = "room.id")
    SeatResponse toSeatResponse(Seat seat);

    @Mapping(target = "id", ignore = true)
    void updateSeat(@MappingTarget Seat seat, SeatUpdateRequest request);
}
