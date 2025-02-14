package sourse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sourse.dto.request.HallCreationRequest;
import sourse.dto.request.HallUpdateRequest;
import sourse.dto.response.HallResponse;
import sourse.entity.Hall;
@Mapper(componentModel = "spring")

public interface HallMapper {
    @Mapping(target = "floor", ignore = true)
    Hall toHall(HallCreationRequest hall);
    HallResponse toHallResponse(Hall hall);
    @Mapping(target = "id", ignore = true)
    void updateHall(@MappingTarget Hall hall, HallUpdateRequest request);
}
