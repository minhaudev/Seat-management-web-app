package sourse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sourse.dto.response.SeatResponse;
import sourse.entity.User;
import sourse.repository.UserRepository;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class WebSocketService extends TextWebSocketHandler {


    @Autowired
    private UserRepository userRepository;
    private final Map<String, Set<WebSocketSession>> roomSessions = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Set<WebSocketSession>> roleSessions = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = getRoomIdFromSession(session);
        roomSessions.computeIfAbsent(roomId, k -> Collections.synchronizedSet(new HashSet<>())).add(session);


        String role = getRoleFromSession(session);
        roleSessions.computeIfAbsent(role, k -> Collections.synchronizedSet(new HashSet<>())).add(session);


        log.info("New WebSocket connection in room {}: {}, Role: {}", roomId, session.getId(), role);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String roomId = getRoomIdFromSession(session);
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }

        String role = getRoleFromSession(session);
        Set<WebSocketSession> roleSessionSet = roleSessions.get(role);
        if (roleSessionSet != null) {
            roleSessionSet.remove(session);
            if (roleSessionSet.isEmpty()) {
                roleSessions.remove(role);
            }
        }

        log.info("WebSocket connection closed in room {}: {}, Role: {}", roomId, session.getId(), role);
    }



    public void sendToSuperUsers(SeatResponse seatResponse, String type, String message) {
        List<User> superUsers = userRepository.findAdmins();

        if (superUsers.isEmpty()) {
            log.info("Không tìm thấy user nào có role SUPERUSER");
            return;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("type", type);
        response.put("message", message != null ? message : "Update received");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonResponse = objectMapper.writeValueAsString(response);

            Set<WebSocketSession> sessions = roleSessions.get("SUPERUSER");
            if (sessions != null) {
                for (WebSocketSession session : sessions) {
                    try {
                        session.sendMessage(new TextMessage(jsonResponse));
                        log.info("JSON message sent to SUPERUSER session: {}", session.getId());
                    } catch (IOException e) {
                        log.error("Error sending WebSocket message to SUPERUSER", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error converting response to JSON", e);
        }
    }


    public void sendSeatUpdateNotification(String roomId, SeatResponse seatResponse, String type, String message) {
        Set<WebSocketSession> sessions = roomSessions.get(roomId);

        if (sessions == null) {
            System.out.println("Không tìm thấy session nào cho roomId: " + roomId);
            return;
        }

        if (message == null || message.isEmpty()) {
            message = (seatResponse != null) ? "Seat updated: " + seatResponse.toString() : "update received";
        }
        Map<String, Object> response = new HashMap<>();
        response.put("type", type);
        response.put("message", message);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonResponse = objectMapper.writeValueAsString(response);
            for (WebSocketSession session : sessions) {
                try {
                    System.out.println("Sending JSON message to room");
                    session.sendMessage(new TextMessage(jsonResponse));
                } catch (IOException e) {
                    log.error("Error sending WebSocket message", e);
                }
            }
        } catch (Exception e) {
            log.error("Error converting response to JSON", e);
        }
    }


    private String getRoomIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("roomId=")) {
            return query.split("roomId=")[1];
        }
        return "default";
    }

    private String getRoleFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("role=")) {
            return query.split("role=")[1];
        }
        return "USER";
    }
}
