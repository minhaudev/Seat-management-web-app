package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.FloorCreationRequest;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.response.FloorResponse;
import sourse.dto.response.RoomResponse;
import sourse.service.FloorService;
import sourse.service.RoomService;

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
}
