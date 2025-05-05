package sourse.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sourse.core.ApiResponse;
import sourse.dto.request.TeamCreationRequest;
import sourse.dto.response.TeamNameResponse;
import sourse.dto.response.TeamResponse;
import sourse.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("api/teams")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamController {
    private final TeamService teamService;

    @PostMapping()
    ApiResponse<TeamResponse> store (@RequestBody @Valid TeamCreationRequest request) {
        ApiResponse<TeamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(teamService.store(request));
        return apiResponse;
    }
    @PatchMapping("/{id}")
    ApiResponse<TeamResponse> update (@RequestBody @Valid TeamCreationRequest request, @PathVariable String id) {
        ApiResponse<TeamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(teamService.update(request, id));
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    ApiResponse<Void> delete (@PathVariable String id) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        teamService.delete(id);
        return ApiResponse.<Void>builder().message("Deleted team successful").build();
    }
    @GetMapping("/names")
    ApiResponse<List<TeamNameResponse>> showAllName(){
        ApiResponse<List<TeamNameResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(teamService.showAllName());
        return apiResponse;

    }
    @GetMapping()
    ApiResponse<List<TeamResponse>> index(){
        ApiResponse<List<TeamResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(teamService.index());
        return apiResponse;
    }
    @GetMapping("/{id}")
    ApiResponse<TeamResponse> show (@PathVariable String id) {
        ApiResponse<TeamResponse> apiResponse = new ApiResponse<>();
        TeamResponse response = teamService.show(id);
         apiResponse.setData(response);
        return apiResponse;
    }
}
