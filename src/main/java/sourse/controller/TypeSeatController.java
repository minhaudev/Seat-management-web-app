package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.TypeSeatCreationRequest;
import sourse.dto.response.TypeSeatResponse;
import sourse.service.TypeSeatService;

import java.util.List;


@RestController
@RequestMapping("api/typeseats")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TypeSeatController {

    private final TypeSeatService typeSeatService;
    @PostMapping()
    public ApiResponse<TypeSeatResponse> store(@RequestBody @Valid TypeSeatCreationRequest request) {
        ApiResponse<TypeSeatResponse> apiResponse = new ApiResponse<TypeSeatResponse>();
        apiResponse.setData(typeSeatService.store(request));
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    ApiResponse <Void> delete(@PathVariable String id) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        typeSeatService.delete(id);
        return ApiResponse.<Void>builder().message("Deleted type seat!").build();
    }
    @GetMapping()
    ApiResponse<List<TypeSeatResponse>> index() {
        ApiResponse<List<TypeSeatResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(typeSeatService.index());
        return apiResponse;
    }
}
