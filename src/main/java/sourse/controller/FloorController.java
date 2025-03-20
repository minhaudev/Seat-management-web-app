package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.FloorCreationRequest;
import sourse.dto.request.FloorUpdateRequest;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.response.FloorResponse;
import sourse.dto.response.RoomResponse;
import sourse.service.FloorService;
import sourse.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("api/floors")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FloorController {
    FloorService floorService;
    @PostMapping()
    ApiResponse<FloorResponse> store (@RequestBody @Valid FloorCreationRequest request){
        ApiResponse<FloorResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(floorService.store(request));
        return apiResponse;
    }
    @GetMapping()
    ApiResponse<List<FloorResponse>> index(){
        ApiResponse<List<FloorResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(floorService.index());
        return  apiResponse;
    }
    @GetMapping("/{id}")
    ApiResponse<FloorResponse> show(@PathVariable String id){
        ApiResponse<FloorResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(floorService.show(id));
        return apiResponse;
    }
    @PatchMapping ("/{id}")
    ApiResponse<FloorResponse> update(@PathVariable String id, @RequestBody @Valid FloorUpdateRequest request){
        ApiResponse<FloorResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(floorService.update(id, request));
        return  apiResponse;
    }
    @DeleteMapping("/{id}")
    ApiResponse<Void> delete(@PathVariable String id){
        floorService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Floor deleted successfully")
                .build();
    }
}
