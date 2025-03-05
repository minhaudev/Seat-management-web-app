package sourse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import sourse.dto.request.*;
import sourse.dto.response.HallResponse;
import sourse.dto.response.RoleResponse;
import sourse.dto.response.UserResponse;
import sourse.entity.Hall;
import sourse.entity.Permission;
import sourse.entity.Role;
import sourse.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")

public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest role);
    RoleResponse toRoleResponse(Role role);
    @Mapping(target = "name", ignore = true)
    void updateRole(@MappingTarget Role role, RoleUpdateRequest request);

    default Set<Permission> mapPermissions(Set<String> permissions) {
        if (permissions == null) return null;
        return permissions.stream()
                .map(Permission::new)
                .collect(Collectors.toSet());
    }
    List<RoleResponse> toRoleResponseList(List<Role> roles);
}
