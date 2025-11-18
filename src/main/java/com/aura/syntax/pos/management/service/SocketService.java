package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.OrderItemsDto;
import com.aura.syntax.pos.management.api.dto.OrdersDto;
import com.aura.syntax.pos.management.config.JwtTokenDto;
import com.aura.syntax.pos.management.config.TokenProperties;
import com.aura.syntax.pos.management.config.WebSocketSessionStore;
import com.aura.syntax.pos.management.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {

    private final OrdersRepository ordersRepository;

    private final TokenProperties tokenProperties;

    private static final Long CHEF_ROLE_ID = 3L;

    public String getTokenForSession(String sessionId) {
        return WebSocketSessionStore.getToken(sessionId);
    }


    public OrdersDto getOrdersForSocket(String token) {

        JwtTokenDto dto = tokenProperties.getTokenFromHeader(token);
        Long roleId = dto.getRoleId();

        if (roleId.equals(CHEF_ROLE_ID)) {
            log.info("User is CHEF → Skipping WebSocket order response.");
            return null;
        }
        OrdersDto ordersDto = ordersRepository.getOrderForSocket();

        Set<OrderItemsDto> orderItemsDtos = ordersRepository.getOrderItemsUnderOrderForSocket(ordersDto.getId());

        Double subTotal = orderItemsDtos.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        Double total = 0.0;
        if (ordersDto.getTaxAmount() != null && ordersDto.getDiscountAmount() != null) {
            total = subTotal + ordersDto.getTaxAmount() - ordersDto.getDiscountAmount();
        } else if (ordersDto.getTaxAmount() != null && ordersDto.getDiscountAmount() == null) {
            total = subTotal + ordersDto.getTaxAmount();
        } else if (ordersDto.getTaxAmount() == null && ordersDto.getDiscountAmount() != null) {
            total = subTotal - ordersDto.getDiscountAmount();
        } else {
            total = subTotal;
        }
        ordersDto.setSubTotal(subTotal);
        ordersDto.setTotalAmount(total);

        int avgPreparationTime = 0;
        if (orderItemsDtos != null && !orderItemsDtos.isEmpty()) {
            Integer totalPrepTime = orderItemsDtos.stream()
                    .mapToInt(item -> item.getPreparationTime() != null ? item.getPreparationTime() : 0)
                    .sum();
            avgPreparationTime = totalPrepTime / orderItemsDtos.size();
        }
        ordersDto.setOrderPreparationTime(avgPreparationTime);
        ordersDto.setOrderItemsDtos(orderItemsDtos);
        return ordersDto;
    }
}
