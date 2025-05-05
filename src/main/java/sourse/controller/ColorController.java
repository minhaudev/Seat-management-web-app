package sourse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sourse.dto.response.ColorInfoResponse;
import sourse.service.ColorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/colors")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public ResponseEntity<Map<String, List<ColorInfoResponse>>> getAllColors() {
        Map<String, List<ColorInfoResponse>> response = new HashMap<>();

        List<ColorInfoResponse> projectColors = colorService.getAllProjectColors();
        List<ColorInfoResponse> teamColors = colorService.getAllTeamColors();

        response.put("projects", projectColors);
        response.put("teams", teamColors);

        return ResponseEntity.ok(response);
    }
}