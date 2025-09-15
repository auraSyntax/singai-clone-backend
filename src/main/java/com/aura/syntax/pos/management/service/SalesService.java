package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.OrderItemsDto;
import com.aura.syntax.pos.management.api.dto.OrdersDto;
import com.aura.syntax.pos.management.api.dto.SalesDto;
import com.aura.syntax.pos.management.enums.PaymentMethod;
import com.aura.syntax.pos.management.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final OrdersRepository ordersRepository;

    public SalesDto getAllSales(LocalDate startDate, LocalDate endDate) {
        SalesDto salesDto = new SalesDto();

        List<OrdersDto> ordersDtos = ordersRepository.getAllOrders(startDate, endDate);

        double totalPaidByCard = 0.0;
        double totalPaidByCash = 0.0;

        for (OrdersDto ordersDto : ordersDtos) {
            List<OrderItemsDto> orderItemsDtos = ordersRepository.getAllOrderItemsByOrder(ordersDto.getId());
            double subTotal = orderItemsDtos.stream()
                    .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                    .sum();

            double total;
            if (ordersDto.getTaxAmount() != null && ordersDto.getDiscountAmount() != null) {
                total = subTotal + ordersDto.getTaxAmount() - ordersDto.getDiscountAmount();
            } else if (ordersDto.getTaxAmount() != null) {
                total = subTotal + ordersDto.getTaxAmount();
            } else if (ordersDto.getDiscountAmount() != null) {
                total = subTotal - ordersDto.getDiscountAmount();
            } else {
                total = subTotal;
            }

            ordersDto.setTotalAmount(total);

            if (PaymentMethod.CARD.getMappedValue().equalsIgnoreCase(ordersDto.getPaymentMethod())) {
                totalPaidByCard += total;
            } else if (PaymentMethod.CASH.getMappedValue().equalsIgnoreCase(ordersDto.getPaymentMethod())) {
                totalPaidByCash += total;
            }
        }

        salesDto.setTotalSalesByCard(totalPaidByCard);
        salesDto.setTotalSalesByCash(totalPaidByCash);
        return salesDto;

    }

}
