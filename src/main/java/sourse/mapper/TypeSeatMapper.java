package sourse.mapper;

import org.mapstruct.*;
import sourse.dto.request.TeamCreationRequest;
import sourse.dto.request.TypeSeatCreationRequest;
import sourse.dto.response.TeamResponse;
import sourse.dto.response.TypeSeatResponse;
import sourse.entity.Team;
import sourse.entity.TypeSeat;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TypeSeatMapper {
    TypeSeat toTypeSeat(TypeSeatCreationRequest typeSeat);
    TypeSeatResponse toTypeSeatResponse(TypeSeat typeSeat);
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTypeSeatFromRequest( TypeSeatCreationRequest request, @MappingTarget TypeSeat typeSeat);
    List<TypeSeatResponse> toTypeSeatRepository(List<TypeSeat> typeSeat);
}
