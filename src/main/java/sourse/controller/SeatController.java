package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.request.SeatCreationRequest;
import sourse.dto.request.SeatUpdateRequest;
import sourse.dto.response.RoomResponse;
import sourse.dto.response.SeatResponse;
import sourse.service.RoomService;
import sourse.service.SeatService;

@RestController
@RequestMapping("api/seats")    
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeatController {
    SeatService seatService;
    @PostMapping()
    ApiResponse<SeatResponse> store (@RequestBody @Valid SeatCreationRequest request){
        ApiResponse<SeatResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(seatService.store(request));
        return apiResponse;
    }
    @PatchMapping("/{id}")
    ApiResponse<SeatResponse> update (@PathVariable String id,  @RequestBody @Valid SeatUpdateRequest request){
        ApiResponse<SeatResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(seatService.update(id, request));
        return apiResponse;
    }
    @GetMapping("/{id}")
    ApiResponse<SeatResponse> show (@PathVariable String id){
        ApiResponse<SeatResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(seatService.show(id));
        return apiResponse;
    }
    @DeleteMapping("/{id}")
  public  ApiResponse<Void> delete (@PathVariable String id){
        seatService.delete(id);
        return ApiResponse.<Void>builder().message("Deleted seat successful").build();
    }

}
