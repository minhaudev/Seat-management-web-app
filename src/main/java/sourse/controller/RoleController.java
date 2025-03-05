package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.RoleCreationRequest;
import sourse.dto.request.RoleUpdateRequest;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.response.RoleResponse;
import sourse.dto.response.RoomResponse;
import sourse.entity.Role;
import sourse.service.RoleService;
import sourse.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("api/roles")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;
    @PostMapping()
    ApiResponse<RoleResponse> store (@RequestBody @Valid RoleCreationRequest request){
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(roleService.store(request));
        return apiResponse;
    }
    @PatchMapping("/{name}")
    ApiResponse<RoleResponse> update (@PathVariable String name, @RequestBody @Valid RoleUpdateRequest request){
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(roleService.update(name, request));
        return apiResponse;
    }
    @GetMapping("/{name}")
    ApiResponse<RoleResponse> show (@PathVariable String name){
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(roleService.show(name));
        return apiResponse;
    }
    @DeleteMapping("/{name}")
    ApiResponse<Void> delete (@PathVariable String name) {
        roleService.delete(name);
        return ApiResponse.<Void>builder().message("Deleted role successful!").build();

    }
    @GetMapping("")
    ApiResponse<List<RoleResponse>> index() {
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
         apiResponse.setData(roleService.index());
        return apiResponse;
    }
}
