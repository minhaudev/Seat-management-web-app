package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import sourse.core.ApiResponse;
import sourse.dto.request.PermissionCreationRequest;
import sourse.dto.response.PermissionResponse;
import sourse.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("api/permissions")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;
    private final RestClient.Builder builder;

    @PostMapping()
    ApiResponse<PermissionResponse> store (@RequestBody @Valid PermissionCreationRequest request){
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(permissionService.store(request));
        return apiResponse;
    }
    @GetMapping("/{name}")
    ApiResponse<PermissionResponse> show(@PathVariable String name){
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(permissionService.show(name));
        return apiResponse;
    }
    @DeleteMapping("/{name}")
    ApiResponse<Void> delete(@PathVariable String name){
        permissionService.delete(name);
        return ApiResponse.<Void>builder().message("deleted permission successful!").build();
    }
   @GetMapping("")
    ApiResponse<List<PermissionResponse>> index(){
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
         apiResponse.setData(permissionService.index());
       return apiResponse;
   }

}
