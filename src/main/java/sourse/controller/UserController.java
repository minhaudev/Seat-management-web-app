package sourse.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.UserCreationRequest;
import sourse.dto.request.UserUpdateRequest;
import sourse.dto.response.RoomResponse;
import sourse.dto.response.UserInRoomResponse;
import sourse.dto.response.UserResponse;
import sourse.service.UserService;

import java.util.List;

@Slf4j
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

        @GetMapping("")
        ApiResponse<List<UserResponse>> index() {
            ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
            apiResponse.setData(userService.index());
            return apiResponse;
        }
        @GetMapping("/info")
    ApiResponse<UserResponse> showInfo() {
            ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
            apiResponse.setData(userService.showInfo());
            return apiResponse;
        }
        @GetMapping("/room/{id}")
    ApiResponse<List<UserInRoomResponse>> userInRoom(@PathVariable String id) {
            ApiResponse<List<UserInRoomResponse>> apiResponse = new ApiResponse<>();
            apiResponse.setData(userService.userInRoom(id));
            return apiResponse;
        }
    }
