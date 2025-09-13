package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.OrderItemsDto;
import com.aura.syntax.pos.management.api.dto.OrdersDto;
import com.aura.syntax.pos.management.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final OrdersRepository ordersRepository;

    public OrdersDto getOrdersForSocket() {
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
        }else {
            total = subTotal;
        }
        ordersDto.setSubTotal(subTotal);
        ordersDto.setTotalAmount(total);

        Integer avgPreparationTime = 0;
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
