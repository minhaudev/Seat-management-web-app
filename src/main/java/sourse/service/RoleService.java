   package sourse.service;

   import lombok.AccessLevel;
   import lombok.RequiredArgsConstructor;
   import lombok.experimental.FieldDefaults;
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.stereotype.Service;
   import sourse.dto.request.RoleCreationRequest;
   import sourse.dto.response.RoleResponse;
   import sourse.entity.Permission;
   import sourse.entity.Role;
   import sourse.mapper.RoleMapper;
   import sourse.repository.PermissionRepository;
   import sourse.repository.RoleRepository;

   import java.util.HashSet;
   import java.util.Set;

   @Slf4j
   @Service
   @RequiredArgsConstructor
   @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
   public class RoleService {
      
       RoleRepository roleRepository;
       RoleMapper roleMapper;
      private final PermissionRepository permissionRepository;

      public RoleResponse store(RoleCreationRequest request) {

         var role = roleMapper.toRole(request);
         var permissions = permissionRepository.findAllById(request.getPermissions());
         role.setPermissions(new HashSet<>(permissions));
       role =   roleRepository.save(role);
         return roleMapper.toRoleResponse(role);
      }
   }
  
