package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.RoleCreationRequest;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.response.RoleResponse;
import sourse.dto.response.RoomResponse;
import sourse.entity.Role;
import sourse.service.RoleService;
import sourse.service.RoomService;

@RestController
@RequestMapping("api/roles")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;
    @PostMapping()
    ApiResponse<Role> store (@RequestBody @Valid RoleCreationRequest request){
        ApiResponse<Role> apiResponse = new ApiResponse<>();
        apiResponse.setData(roleService.store(request));
        return apiResponse;
    }

}
