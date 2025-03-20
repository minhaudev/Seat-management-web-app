package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.FloorCreationRequest;
import sourse.dto.request.HallCreationRequest;
import sourse.dto.request.HallUpdateRequest;
import sourse.dto.response.FloorResponse;
import sourse.dto.response.HallResponse;
import sourse.service.FloorService;
import sourse.service.HallService;

import java.util.List;

@RestController
@RequestMapping("api/halls")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HallController {
    HallService hallService;
    @PostMapping()
    ApiResponse<HallResponse> store (@RequestBody @Valid HallCreationRequest request){
        ApiResponse<HallResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(hallService.store(request));
        return apiResponse;
    }
    @PatchMapping("/{id}")
    ApiResponse<HallResponse> update(@PathVariable String id, @RequestBody @Valid HallUpdateRequest request){
        ApiResponse<HallResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(hallService.update(id,request));
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    ApiResponse<Void> delete(@PathVariable String id){
      hallService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Hall deleted successfully")
                .build();
    }
    @GetMapping
    ApiResponse<List<HallResponse>> findAll(){
        ApiResponse<List<HallResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(hallService.index());
        return apiResponse;
    }
    @GetMapping("/floor/{id}")
    ApiResponse<List<HallResponse>> findById(@PathVariable String id){
        ApiResponse<List<HallResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(hallService.getAllFloor(id));
        return apiResponse;
    }
    @GetMapping("/{id}")
    ApiResponse<HallResponse> show(@PathVariable String id){
        ApiResponse<HallResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(hallService.show(id));
        return apiResponse;
    }
}
