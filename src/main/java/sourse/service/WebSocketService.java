package sourse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sourse.dto.response.SeatResponse;
import java.io.IOException;
import java.util.*;
@Slf4j
@Component
public class WebSocketService extends TextWebSocketHandler {
    // Map chứa danh sách client theo roomId
    private final Map<String, Set<WebSocketSession>> roomSessions = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = getRoomIdFromSession(session);
        roomSessions.computeIfAbsent(roomId, k -> Collections.synchronizedSet(new HashSet<>())).add(session);
        System.out.println("roomId: Session 1 " + roomId + "  " +  roomSessions.get(roomId).size());
        log.info("New WebSocket connection in room {}: {}", roomId, session.getId());
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
        log.info("WebSocket connection closed in room {}: {}", roomId, session.getId());
    }


    public void sendSeatUpdateNotification(String roomId, SeatResponse seatResponse, String message) {
        Set<WebSocketSession> sessions = roomSessions.get(roomId);

        if (sessions == null) {
            System.out.println(" Không tìm thấy session nào cho roomId: " + roomId);
            return;
        }
        if (message == null || message.isEmpty()) {
            message = (seatResponse != null) ? "Seat updated: " + seatResponse.toString() : " update received";
        }
        for (WebSocketSession session : sessions) {
            try {
                System.out.println("message data");
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("Error sending WebSocket message", e);
            }
        }
    }

    private String getRoomIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("roomId=")) {
            return query.split("roomId=")[1];
        }
        return "default";
    }
}
