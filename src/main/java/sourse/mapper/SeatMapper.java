package sourse.mapper;

import org.mapstruct.*;

import sourse.dto.request.SeatCreationRequest;
import sourse.dto.request.SeatUpdateRequest;
import sourse.dto.response.SeatResponse;
import sourse.entity.Seat;
import sourse.enums.EnumType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "typeSeat", source = "typeSeat")
    Seat toSeat(SeatCreationRequest seat);



    @Mapping(target = "user", source = "user")
    @Mapping(target = "roomId", source = "room.id")
    SeatResponse toSeatResponse(Seat seat);
    List<SeatResponse> toSeatResponseList(List<Seat> seats);

    @Mapping(target = "id", ignore = true)
    void updateSeat(@MappingTarget Seat seat, SeatUpdateRequest request);
}
