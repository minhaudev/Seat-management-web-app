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
import sourse.dto.response.FloorResponse;
import sourse.dto.response.HallResponse;
import sourse.service.FloorService;
import sourse.service.HallService;

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
    @GetMapping("/{id}")
    ApiResponse<HallResponse> show(@PathVariable String id){
        ApiResponse<HallResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(hallService.show(id));
        return apiResponse;
    }
}
