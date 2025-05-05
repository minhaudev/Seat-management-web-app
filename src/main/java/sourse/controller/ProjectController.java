package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.ProjectCreationRequest;
import sourse.dto.response.ProjectNameResponse;
import sourse.dto.response.ProjectResponse;
import sourse.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("api/projects")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping()
    ApiResponse<ProjectResponse> store (@RequestBody @Valid ProjectCreationRequest request) {
        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(projectService.store(request));
        return apiResponse;
    }
    @PatchMapping("/{id}")
    ApiResponse<ProjectResponse> update (@RequestBody @Valid ProjectCreationRequest request, @PathVariable String id) {
        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(projectService.update(request, id));
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    ApiResponse<Void> delete (@PathVariable String id) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        projectService.delete(id);
        return ApiResponse.<Void>builder().message("Deleted project successful").build();
    }
    @GetMapping("/names")
    ApiResponse<List<ProjectNameResponse>> showAllName(){
        ApiResponse<List<ProjectNameResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(projectService.showAllName());
        return apiResponse;

    }
    @GetMapping()
    ApiResponse<List<ProjectResponse>> index(){
        ApiResponse<List<ProjectResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(projectService.index());
        return apiResponse;
    }
    @GetMapping("/{id}")
    ApiResponse<ProjectResponse> show (@PathVariable String id) {
        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>();
        ProjectResponse response = projectService.show(id);
         apiResponse.setData(response);
        return apiResponse;
    }
}
