package sourse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import sourse.dto.request.HallCreationRequest;
import sourse.dto.request.HallUpdateRequest;
import sourse.dto.request.RoleCreationRequest;
import sourse.dto.response.HallResponse;
import sourse.dto.response.RoleResponse;
import sourse.entity.Hall;
import sourse.entity.Role;

@Mapper(componentModel = "spring")

public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest role);
    RoleResponse toRoleResponse(Role role);
}
