package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sourse.core.ApiResponse;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.request.UserCreationRequest;
import sourse.dto.response.RoomResponse;
import sourse.dto.response.UserResponse;
import sourse.entity.Room;
import sourse.entity.RoomChange;
import sourse.service.RedisService;
import sourse.service.RoomService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/rooms")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {
    RoomService roomService;
    private final RedisService redisService;

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
    @GetMapping("/hall/{id}")
    ApiResponse <List<RoomResponse>> roomInHall(@PathVariable String id){
        ApiResponse<List<RoomResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(roomService.roomInHall(id));
        return apiResponse;
    }
 @PutMapping("/{id}/update-objects")
    ApiResponse<RoomResponse>  updateRoomObjects(@PathVariable String id, @RequestBody List<Room.ObjectData> objects)
 {
 ApiResponse<RoomResponse> apiResponse = new ApiResponse<>();
 apiResponse.setData(roomService.updateRoomObjects(id, objects));
 return apiResponse;
 }
    @PostMapping("/{id}/upload")
    ApiResponse<RoomResponse> uploadImage(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setData(roomService.uploadImage(id, file));
       return apiResponse;
    }
    @DeleteMapping("/{roomId}/objects/{objectId}")
    ApiResponse<Void> deleteRoomObjects(@PathVariable String roomId, @PathVariable String objectId) {
        roomService.deleteObject(roomId, objectId);
        return ApiResponse.<Void>builder().message("Delete object successful").build();
    }


    @GetMapping("/update/layout")
    public ResponseEntity<List<RoomChange>> getAllRoomChanges() {
        List<RoomChange> roomChanges = redisService.getAllRoomChanges("room_changes");
        return ResponseEntity.ok(roomChanges);
    }
    @PostMapping("/approve/{roomId}")
    public  ApiResponse<RoomResponse> approve(@PathVariable String roomId) {
        roomService.approve(roomId);
        return ApiResponse.<RoomResponse>builder().message("Approve object successful").build();
    }
    @DeleteMapping("/reject/{roomId}")
    public   ApiResponse<RoomResponse> reject(@PathVariable String roomId) {
        redisService.removeRoomChange("room_changes", roomId);
        return ApiResponse.<RoomResponse>builder().message("Reject object successful").build();
    }
    }
