package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;

import sourse.dto.request.SeatCreationRequest;
import sourse.dto.request.SeatUpdateRequest;

import sourse.dto.response.SeatResponse;
import sourse.dto.response.SeatUserResponse;

import sourse.enums.EnumType;
import sourse.service.SeatService;
import sourse.service.WebSocketService;

import java.util.List;
import java.util.Map;

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
    ApiResponse<Void> delete (@PathVariable String id){
        seatService.delete(id);
        return ApiResponse.<Void>builder().message("Deleted seat successful").build();
    }
    @GetMapping
    public ApiResponse<Map<String, Object>> index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
        apiResponse.setData(seatService.index(page, size));
        return apiResponse;
    }

    @GetMapping("/{id}/room")
    ApiResponse <Page<SeatResponse>> listSeatInRoom(@PathVariable String id,   @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(required  = false) EnumType.SeatStatus status ) {

        ApiResponse<Page<SeatResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(seatService.listSeatInRoom(id, page, size,status));
        return apiResponse;
    }

    @PostMapping("/{id}/assign")
     ApiResponse<SeatResponse> assignment (@PathVariable String id, @RequestParam String idUser) {
          ApiResponse<SeatResponse> apiResponse = new ApiResponse<>();
          apiResponse.setData(seatService.assignment(id, idUser));

          return  apiResponse;
    }
    @PostMapping("/{id}/reassign")
     ApiResponse<SeatResponse> reAssignment (@PathVariable String id, @RequestParam String idSeat){
        ApiResponse<SeatResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(seatService.reAssignment(id, idSeat));
        return  apiResponse;
    }
    @GetMapping("/{id}/user")
      ApiResponse<SeatUserResponse> seatUser (@PathVariable String id) {
        ApiResponse<SeatUserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(seatService.seatUser(id));
        return  apiResponse;
    }
}
