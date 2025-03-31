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
