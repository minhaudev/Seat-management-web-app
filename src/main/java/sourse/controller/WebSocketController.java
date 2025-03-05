//package sourse.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Slf4j
//@Controller
//public class WebSocketController {
//
//    @MessageMapping("/send-message") // Nhận tin nhắn từ client
//    @SendTo("/topic/receive-message") // Gửi phản hồi đến client đang subscribe
//    public String handleMessage(String message) {
//        log.info("Received message: {}", message);
//        return "Server received: " + message;
//    }
//}