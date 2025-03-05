package sourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sourse.dto.request.RoleCreationRequest;
import sourse.dto.request.RoleUpdateRequest;
import sourse.dto.response.RoleResponse;
import sourse.entity.Permission;
import sourse.entity.Role;
import sourse.entity.Room;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.RoleMapper;
import sourse.repository.PermissionRepository;
import sourse.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

   RoleRepository roleRepository;
   RoleMapper roleMapper;
   private final PermissionRepository permissionRepository;
   public Role findByName(String name) {
      return roleRepository.findByName(name)
              .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
   }
   @PreAuthorize("hasAnyRole('SUPERUSER')")
   public RoleResponse store(RoleCreationRequest request) {
      if (roleRepository.existsByName(request.getName())) {
         throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
      }
      Role role = roleMapper.toRole(request);
      role.setName(request.getName());
      roleMapper.toRole(request);
      var permissions = permissionRepository.findAllById(request.getPermissions());
      role.setPermissions(new HashSet<>(permissions));
      role =  roleRepository.save(role);
      return roleMapper.toRoleResponse(role);
   }
   @PreAuthorize("hasAnyRole('SUPERUSER')")
   public RoleResponse update (String name, RoleUpdateRequest request){
      Role role = this.findByName(name);
      var permissions = permissionRepository.findAllById(request.getPermissions());
      role.setPermissions(new HashSet<>(permissions));
      role =  roleRepository.save(role);
      roleMapper.updateRole(role, request);
      role =   roleRepository.save(role);
      return roleMapper.toRoleResponse(role);
   };

   @PreAuthorize("hasAnyRole('SUPERUSER')")
   public List<RoleResponse> index() {
      return  roleMapper.toRoleResponseList(roleRepository.findAll());
   }
   public RoleResponse show (String name) {
     Role role = this.findByName(name);
     return roleMapper.toRoleResponse(role);
   }
   @PreAuthorize("hasAnyRole('SUPERUSER')")
   public void delete(String name) {
      Role role = this.findByName(name);
      roleRepository.delete(role);
   }

}

