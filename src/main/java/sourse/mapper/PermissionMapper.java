package sourse.mapper;

import org.mapstruct.Mapper;
import sourse.dto.request.PermissionCreationRequest;
import sourse.dto.request.RoleCreationRequest;
import sourse.dto.response.PermissionResponse;
import sourse.dto.response.RoleResponse;
import sourse.entity.Permission;
import sourse.entity.Role;

@Mapper(componentModel = "spring")

public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest permission);
    PermissionResponse toPermissionResponse(Permission permission);
}
