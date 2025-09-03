package com.aura.syntax.pos.management.config.websocket;

import com.aura.syntax.pos.management.repository.OrdersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final SimpMessagingTemplate messagingTemplate;

    public static Set<String> connectedUserIds = ConcurrentHashMap.newKeySet();

    private final OrdersRepository ordersRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            StompCommand command = accessor.getCommand();

            if (StompCommand.CONNECT.equals(command)) {
                notifyKitchen();
            }
        }

        return message;
    }

    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String branchId = (String) accessor.getSessionAttributes().get("branchId");

        if (branchId != null) {
            connectedUserIds.remove(branchId);
            log.info("❌ Removed disconnected userId {}", branchId);
        }
    }

    public void notifyKitchen() {
        new Thread(() -> {
            try {
                LocalDate date = LocalDate.now();
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

                List<OrdersWebSocketDto> ordersWebSocketDtos = ordersRepository.getAllOrdersForKitchen(startOfDay,endOfDay);

                String json = new ObjectMapper().writeValueAsString(ordersWebSocketDtos);
                Thread.sleep(2000);
                messagingTemplate.convertAndSend("/topicPath/confirmedOrders", json);
                log.info("📤 Sent orders to kitchen: {}", ordersWebSocketDtos);
            } catch (Exception e) {
                log.error("Error sending orders", e);
            }
        }).start();

    }

}


