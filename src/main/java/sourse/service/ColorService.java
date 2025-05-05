//package sourse.service;
//
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ColorService {
//
//    private static final Logger logger = LoggerFactory.getLogger(ColorService.class);
//    private final RedisTemplate<String, String> redisTemplate;
//    private static final String COLOR_HASH_KEY = "team_project_colors";
//    private static final String DEFAULT_COLOR = "#808080";
//
//    public String getColor(String team, String project) {
//
//        if ((team == null || team.isBlank()) && (project == null || project.isBlank())) {
//            return DEFAULT_COLOR;
//        }
//
//        String teamKey = (team != null && !team.isBlank()) ? "team:" + team : null;
//        String projectKey = (project != null && !project.isBlank()) ? "project:" + project : null;
//
//
//        if (teamKey != null) {
//            String teamColor = (String) redisTemplate.opsForHash().get(COLOR_HASH_KEY, teamKey);
//            if (teamColor != null) return teamColor;
//        }
//        if (projectKey != null) {
//
//            String projectColor = (String) redisTemplate.opsForHash().get(COLOR_HASH_KEY, projectKey);
//            if (projectColor != null) return projectColor;
//        }
//
//
//        String newColor = generateRandomColor();
//        if (teamKey != null) redisTemplate.opsForHash().put(COLOR_HASH_KEY, teamKey, newColor);
//        else if (projectKey != null) redisTemplate.opsForHash().put(COLOR_HASH_KEY, projectKey, newColor);
//
//        return newColor;
//    }
//
//    private String generateRandomColor() {
//        String[] colors = {"#FF5733", "#33FF57", "#3357FF", "#F1C40F", "#8E44AD"};
//        return colors[(int) (Math.random() * colors.length)];
//    }
//}

package sourse.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sourse.dto.response.ColorInfoResponse;
import sourse.entity.Project;
import sourse.entity.Team;
import sourse.repository.ProjectRepository;
import sourse.repository.TeamRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColorService {

    private static final Logger logger = LoggerFactory.getLogger(ColorService.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;

    private static final String COLOR_HASH_KEY = "team_project_colors";
    private static final String DEFAULT_COLOR = "#808080";
    private static final String TEAM_COLOR_HASH = "team_colors";
    private static final String PROJECT_COLOR_HASH = "project_colors";
    // Lấy màu theo team hoặc project (nếu 1 trong 2 null)
    public String getColor(String team, String project) {

        if ((team == null || team.isBlank()) && (project == null || project.isBlank())) {
            return DEFAULT_COLOR;
        }

        String teamKey = (team != null && !team.isBlank()) ? "team:" + team : null;
        String projectKey = (project != null && !project.isBlank()) ? "project:" + project : null;

        if (teamKey != null) {
            String teamColor = (String) redisTemplate.opsForHash().get(COLOR_HASH_KEY, teamKey);
            if (teamColor != null) return teamColor;
        }

        if (projectKey != null) {
            String projectColor = (String) redisTemplate.opsForHash().get(COLOR_HASH_KEY, projectKey);
            if (projectColor != null) return projectColor;
        }


        String newColor = generateRandomColor();
        if (teamKey != null) redisTemplate.opsForHash().put(COLOR_HASH_KEY, teamKey, newColor);
        else if (projectKey != null) redisTemplate.opsForHash().put(COLOR_HASH_KEY, projectKey, newColor);

        return newColor;
    }

    public Map<String, List<ColorInfoResponse>> getAllColors() {
        Map<String, List<ColorInfoResponse>> result = new HashMap<>();

        List<ColorInfoResponse> teamColors = teamRepository.findAll().stream()
                .map(team -> {
                    String key = "team:" + team.getId();
                    String color = (String) redisTemplate.opsForHash().get(COLOR_HASH_KEY, key);
                    if (color == null) {
                        color = generateRandomColor();
                        redisTemplate.opsForHash().put(COLOR_HASH_KEY, key, color);
                    }
                    return new ColorInfoResponse(team.getId(), team.getName(), color);
                }).collect(Collectors.toList());

        List<ColorInfoResponse> projectColors = projectRepository.findAll().stream()
                .map(project -> {
                    String key = "project:" + project.getId();
                    String color = (String) redisTemplate.opsForHash().get(COLOR_HASH_KEY, key);
                    if (color == null) {
                        color = generateRandomColor();
                        redisTemplate.opsForHash().put(COLOR_HASH_KEY, key, color);
                    }
                    return new ColorInfoResponse(project.getId(), project.getName(), color);
                }).collect(Collectors.toList());

        result.put("teams", teamColors);
        result.put("projects", projectColors);

        return result;
    }

    private String generateRandomColor() {
        String[] colors = {
                "#FF5733", "#33FF57", "#3357FF", "#F1C40F", "#8E44AD",
                "#2ECC71", "#1ABC9C", "#E74C3C", "#9B59B6", "#34495E"
        };
        return colors[(int) (Math.random() * colors.length)];
    }


    public List<ColorInfoResponse> getAllProjectColors() {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> projectColors = hashOps.entries(PROJECT_COLOR_HASH);
        List<ColorInfoResponse> colorList = new ArrayList<>();

        projectColors.forEach((key, value) -> {
            String[] parts = value.split(",");
            if (parts.length >= 2) {
                colorList.add(new ColorInfoResponse(key, parts[0], parts[1]));
            } else {
                colorList.add(new ColorInfoResponse(key, parts[0], "#FFFFFF")); // Màu mặc định
            }
        });

        return colorList;
    }

    public List<ColorInfoResponse> getAllTeamColors() {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> teamColors = hashOps.entries(TEAM_COLOR_HASH);
        List<ColorInfoResponse> colorList = new ArrayList<>();

        teamColors.forEach((key, value) -> {
            String[] parts = value.split(",");
            if (parts.length >= 2) {
                colorList.add(new ColorInfoResponse(key, parts[0], parts[1])); // key = id, parts[0] = name, parts[1] = color
            } else {
                colorList.add(new ColorInfoResponse(key, parts[0], "#FFFFFF")); // Màu mặc định
            }
        });

        return colorList;
    }
}
