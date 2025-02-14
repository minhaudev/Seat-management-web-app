package sourse.mapper;

import org.mapstruct.*;
import sourse.dto.request.SeatCreationRequest;
import sourse.dto.request.SeatUpdateRequest;
import sourse.dto.response.SeatResponse;
import sourse.entity.Seat;
import sourse.enums.EnumType;

@Mapper(componentModel = "spring", uses = SeatMapperHelper.class) // 🔹 Sử dụng Helper
public interface SeatMapper {

    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    @Mapping(source = "typeSeat", target = "typeSeat", qualifiedByName = "mapTypeSeat")
    Seat toSeat(SeatCreationRequest seat);

    SeatResponse toSeatResponse(Seat seat);

    @Mapping(target = "id", ignore = true)
    void updateSeat(@MappingTarget Seat seat, SeatUpdateRequest request);
}
