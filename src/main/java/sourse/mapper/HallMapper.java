package sourse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sourse.dto.request.HallCreationRequest;
import sourse.dto.request.HallUpdateRequest;
import sourse.dto.response.FloorResponse;
import sourse.dto.response.HallResponse;
import sourse.entity.Floor;
import sourse.entity.Hall;

import java.util.List;

@Mapper(componentModel = "spring")

public interface HallMapper {
    @Mapping(target = "floor", ignore = true)
    Hall toHall(HallCreationRequest hall);
    @Mapping(target = "floorId",source = "floor.id")
    @Mapping(target = "floorName", source = "floor.name")
    HallResponse toHallResponse(Hall hall);
    @Mapping(target = "id", ignore = true)
    void updateHall(@MappingTarget Hall hall, HallUpdateRequest request);
    List<HallResponse> toHallResponseList(List<Hall> halls);

}
