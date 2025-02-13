package sourse.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.UserCreationRequest;
import sourse.dto.request.UserUpdateRequest;
import sourse.dto.response.UserResponse;
import sourse.entity.User;
import sourse.service.UserService;

    @RestController
    @RequestMapping("api/users")
    @Validated
    public class UserController  {
        @Autowired
        private UserService userService;
        @PostMapping()
        ApiResponse<UserResponse> store (@RequestBody @Valid UserCreationRequest request) {
            ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
            apiResponse.setData(userService.store(request));
            return apiResponse;
        }
        @PatchMapping("/{id}")
        ApiResponse<UserResponse> update (@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
            ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
            apiResponse.setData(userService.update(id,request));
            return apiResponse;
        }
        @DeleteMapping("/{id}")
        public ApiResponse<Void> delete(@PathVariable String id) {
            userService.delete(id);
            return ApiResponse.<Void>builder()
                    .message("User deleted successfully")
                    .build();
        }
        @GetMapping("/{id}")
        ApiResponse<UserResponse> show(@PathVariable String id) {
            ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
            apiResponse.setData(userService.show(id));
            return apiResponse;
        }

    }
