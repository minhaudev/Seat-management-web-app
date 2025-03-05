   package sourse.service;

   import lombok.AccessLevel;
   import lombok.RequiredArgsConstructor;
   import lombok.experimental.FieldDefaults;
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.security.access.prepost.PreAuthorize;
   import org.springframework.stereotype.Service;
   import sourse.dto.request.PermissionCreationRequest;
   import sourse.dto.request.PermissionUpdateRequest;
   import sourse.dto.request.RoleCreationRequest;
   import sourse.dto.response.PermissionResponse;
   import sourse.dto.response.RoleResponse;
   import sourse.entity.Floor;
   import sourse.entity.Permission;
   import sourse.entity.Role;
   import sourse.exception.AppException;
   import sourse.exception.ErrorCode;
   import sourse.mapper.PermissionMapper;
   import sourse.mapper.RoleMapper;
   import sourse.repository.PermissionRepository;
   import sourse.repository.RoleRepository;

   import java.util.List;
   import java.util.stream.Collectors;

   @Slf4j
   @Service
   @RequiredArgsConstructor
   @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
   public class PermissionService {
      
       PermissionRepository permissionRepository;
       PermissionMapper permissionMapper;
       @PreAuthorize("hasAnyRole('SUPERUSER')")
      public PermissionResponse store(PermissionCreationRequest request) {
          if (permissionRepository.existsByName(request.getName())) {
              throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTS);
          }
         Permission permission = permissionRepository.save(permissionMapper.toPermission(request));
         return permissionMapper.toPermissionResponse(permission);
      }
       @PreAuthorize("hasAnyRole('SUPERUSER')")
      public PermissionResponse update(String name, PermissionUpdateRequest request) {
       Permission permission = this.findById(name);
         permissionMapper.updatePermission(permission, request);
         permissionRepository.save(permission);
         return permissionMapper.toPermissionResponse(permission);
      }
       @PreAuthorize("hasAnyRole('SUPERUSER')")
      public PermissionResponse show (String name) {
         Permission permission = this.findById(name);
         return permissionMapper.toPermissionResponse(permission);
      }
       @PreAuthorize("hasAnyRole('SUPERUSER')")
      public void delete(String name) {
         Permission permission = this.findById(name);
         permissionRepository.delete(permission);
      }
       @PreAuthorize("hasAnyRole('SUPERUSER')")
     public List<PermissionResponse> index() {
         List<Permission> permissions = permissionRepository.findAll();

      return permissions.stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toList());}
      public Permission findById(String name) {
         return permissionRepository.findById(name)
                 .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
      }
   }
  
