package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.request.UserCreationRequest;
import sourse.dto.response.RoomResponse;
import sourse.dto.response.UserResponse;
import sourse.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("api/rooms")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {
    RoomService roomService;
    @PostMapping()
    ApiResponse<RoomResponse> store (@RequestBody @Valid RoomCreationRequest request){
        ApiResponse<RoomResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(roomService.store(request));
        return apiResponse;
    }
    @PatchMapping("/{id}")
        ApiResponse <RoomResponse> update (@PathVariable String id,@RequestBody RoomUpdateRequest request) {
            ApiResponse <RoomResponse> apiResponse = new ApiResponse<>();
            apiResponse.setData(roomService.update(id, request));
            return apiResponse;
    }
    @GetMapping("/{id}")
    ApiResponse<RoomResponse> show(@PathVariable String id){
        ApiResponse<RoomResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(roomService.show(id));
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    ApiResponse<Void> delete(@PathVariable String id){
        roomService.delete(id);
        return ApiResponse.<Void>builder().message("Delete room successful").build();
    }
    @GetMapping()
    ApiResponse <Page<RoomResponse>> index(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        ApiResponse <Page<RoomResponse>> apiResponse = new ApiResponse<>();
         apiResponse.setData(roomService.index(page, size));
        return apiResponse;
    }
    }
