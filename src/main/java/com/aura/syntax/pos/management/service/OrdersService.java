package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.*;
import com.aura.syntax.pos.management.config.websocket.WebSocketChannelInterceptor;
import com.aura.syntax.pos.management.entity.OrderItems;
import com.aura.syntax.pos.management.entity.Orders;
import com.aura.syntax.pos.management.entity.Tables;
import com.aura.syntax.pos.management.enums.*;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;

    private static final String PREFIX = "ORD";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int UNIQUE_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();
    private final TablesRepository tablesRepository;
    private final UserRepository userRepository;
    private final MenuItemsRepository menuItemsRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final WebSocketChannelInterceptor webSocketChannelInterceptor;

    @Value("${cloudinary.base.url}")
    private String imagePath;

    public SaveOrderResponseDto addOrder(OrdersDto ordersDto) {

        if (ordersDto.getTableId() != null) {
            Tables table = tablesRepository.findById(ordersDto.getTableId())
                    .orElseThrow(() -> new ServiceException("Table not found", "Bad request", HttpStatus.BAD_REQUEST));
            table.setTableStatus(TableStatus.OCCUPIED);
            tablesRepository.save(table);
        }

        Orders orders = Orders.builder()
                .id(ordersDto.getId())
                .orderNumber(generateOrderNumber())
                .tableId(ordersDto.getTableId())
                .waiterId(ordersDto.getWaiterId())
                .customerName(ordersDto.getCustomerName())
                .customerPhone(ordersDto.getCustomerPhone())
                .orderType(OrderType.fromMappedValue(ordersDto.getOrderType()))
                .orderStatus(OrderStatus.PENDING)
                .taxAmount(ordersDto.getTaxAmount())
                .discountAmount(ordersDto.getDiscountAmount())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(PaymentMethod.fromMappedValue(ordersDto.getPaymentMethod()))
                .notes(ordersDto.getNotes())
                .createdAt(LocalDateTime.now())
                .orderItems(ordersDto.getOrderItemsDtos() != null && !ordersDto.getOrderItemsDtos().isEmpty() ?
                        ordersDto.getOrderItemsDtos().stream()
                                .map(this::convertOrderItems).collect(Collectors.toSet()) : null)
                .build();
        Orders savedOrder = ordersRepository.save(orders);
        return SaveOrderResponseDto.builder()
                .id(savedOrder.getId())
                .waiterId(savedOrder.getWaiterId() != null ?
                        savedOrder.getWaiterId() : null)
                .tableId(savedOrder.getTableId() != null ?
                        savedOrder.getTableId() : null)
                .orderStatus(savedOrder.getOrderStatus() != null ?
                        savedOrder.getOrderStatus().getMappedValue() : null)
                .orderType(savedOrder.getOrderType() != null ?
                        savedOrder.getOrderType().getMappedValue() : null)
                .orderNumber(savedOrder.getOrderNumber())
                .build();
    }

    private OrderItems convertOrderItems(OrderItemsDto orderItemsDto) {
        return OrderItems.builder()
                .id(orderItemsDto.getId())
                .orderId(orderItemsDto.getOrderId())
                .menuItemsId(orderItemsDto.getMenuItemsId())
                .quantity(orderItemsDto.getQuantity())
                .specialInstructions(orderItemsDto.getSpecialInstructions())
                .createdAt(LocalDateTime.now())
                .unitPrice(orderItemsDto.getUnitPrice())
                .totalPrice(orderItemsDto.getTotalPrice())
                .status(OrderStatus.PENDING)
                .isRetail(orderItemsDto.getIsRetail())
                .build();
    }

    public static String generateOrderNumber() {
        StringBuilder uniquePart = new StringBuilder(UNIQUE_LENGTH);

        Integer[] loopArray = new Integer[UNIQUE_LENGTH];
        Arrays.fill(loopArray, 0);

        for (Integer i : loopArray) {
            int index = random.nextInt(CHARACTERS.length());
            uniquePart.append(CHARACTERS.charAt(index));
        }

        return PREFIX + uniquePart;
    }

    public OrdersDto getOrderById(Long id) {
        Orders orders = ordersRepository.findById(id).orElseThrow(() -> new ServiceException("Order not found", "Bad request", HttpStatus.BAD_REQUEST));
        return OrdersDto.builder()
                .id(orders.getId())
                .orderNumber(orders.getOrderNumber())
                .tableId(orders.getTableId())
                .waiterId(orders.getWaiterId())
                .customerName(orders.getCustomerName())
                .customerPhone(orders.getCustomerPhone())
                .orderType(orders.getOrderType() != null ? orders.getOrderType().getMappedValue() : null)
                .orderStatus(orders.getOrderStatus() != null ?
                        orders.getOrderStatus().getMappedValue() : null)
                .taxAmount(orders.getTaxAmount())
                .discountAmount(orders.getDiscountAmount())
                .paymentStatus(orders.getPaymentStatus() != null ?
                        orders.getPaymentStatus().getMappedValue() : null)
                .paymentStatus(orders.getPaymentStatus() != null ?
                        orders.getPaymentStatus().getMappedValue() : null)
                .notes(orders.getNotes())
                .orderItemsDtos(orders.getOrderItems() != null && !orders.getOrderItems().isEmpty() ?
                        orders.getOrderItems().stream()
                                .map(this::convertOrderItems).collect(Collectors.toSet()) : null)
                .tableName(tablesRepository.getTableNameById(orders.getTableId()))
                .waiterName(userRepository.getNameById(orders.getWaiterId()))
                .build();
    }

    private OrderItemsDto convertOrderItems(OrderItems orderItems) {
        return OrderItemsDto.builder()
                .id(orderItems.getId())
                .orderId(orderItems.getOrderId())
                .menuItemsId(orderItems.getMenuItemsId())
                .menuItemName(menuItemsRepository.getMenuItemById(orderItems.getMenuItemsId()))
                .quantity(orderItems.getQuantity())
                .specialInstructions(orderItems.getSpecialInstructions())
                .createdAt(LocalDateTime.now())
                .unitPrice(orderItems.getUnitPrice())
                .totalPrice(orderItems.getTotalPrice())
                .isRetail(orderItems.getIsRetail())
                .status(orderItems.getStatus() != null ? orderItems.getStatus().getMappedValue() : null)
                .build();
    }

    public PaginatedResponseDto<OrdersDto> getAllOrdersPagination(Integer page, Integer size, Long waiterId, String orderType, String orderStatus, String search) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<OrdersDto> ordersDtos = ordersRepository.getAllOrdersPagination(pageable, waiterId, orderType != null && !orderType.isEmpty() ?
                OrderType.fromMappedValue(orderType) : null, orderStatus != null && !orderStatus.isEmpty() ? OrderStatus.fromMappedValue(orderStatus) : null, search);

        List<OrdersDto> ordersDtoList = ordersDtos.getContent();
        ordersDtoList.stream().forEach(ordersDto -> {
            Set<OrderItemsDto> orderItemsDtos = ordersRepository.getAllOrderItems(ordersDto.getId());


            orderItemsDtos.stream().forEach(orderItemsDto -> {
                orderItemsDto.setImageUrl(orderItemsDto.getImageUrl() != null ? imagePath + orderItemsDto.getImageUrl() : null);
                Double subTotal = orderItemsDtos.stream()
                        .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                        .sum();

                Double total = subTotal + ordersDto.getTaxAmount() - ordersDto.getDiscountAmount();
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
            });
            ordersDto.setOrderItemsDtos(orderItemsDtos);
        });
        PaginatedResponseDto<OrdersDto> ordersDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        ordersDtoPaginatedResponseDto.setData(ordersDtoList);
        ordersDtoPaginatedResponseDto.setCurrentPage(page);
        ordersDtoPaginatedResponseDto.setTotalItems(ordersDtos.getTotalElements());
        ordersDtoPaginatedResponseDto.setTotalPages(ordersDtos.getTotalPages());
        ordersDtoPaginatedResponseDto.setPageSize(size);
        ordersDtoPaginatedResponseDto.setHasPrevious(page > 1);
        ordersDtoPaginatedResponseDto.setHasNext(page < ordersDtos.getTotalPages());
        return ordersDtoPaginatedResponseDto;
    }

    public ResponseDto updateOrders(OrdersDto ordersDto) {
        Orders existingOrder = ordersRepository.findById(ordersDto.getId()).orElseThrow(() ->
                new ServiceException("Order not found", "Bad request", HttpStatus.BAD_REQUEST));

        if (ordersDto.getTableId() != null) {
            if (!ordersDto.getTableId().equals(existingOrder.getTableId())) {
                Tables newTable = tablesRepository.findById(ordersDto.getTableId())
                        .orElseThrow(() -> new ServiceException("Table not found", "Bad request", HttpStatus.BAD_REQUEST));
                newTable.setTableStatus(TableStatus.OCCUPIED);
                tablesRepository.save(newTable);

                Tables existingTable = tablesRepository.findById(existingOrder.getTableId())
                        .orElseThrow(() -> new ServiceException("Table not found", "Bad request", HttpStatus.BAD_REQUEST));
                existingTable.setTableStatus(TableStatus.AVAILABLE);

                tablesRepository.save(existingTable);
            }
        }

        existingOrder.setTableId(ordersDto.getTableId());
        existingOrder.setWaiterId(ordersDto.getWaiterId());
        existingOrder.setCustomerName(ordersDto.getCustomerName());
        existingOrder.setCustomerPhone(ordersDto.getCustomerPhone());
        existingOrder.setOrderType(OrderType.fromMappedValue(ordersDto.getOrderType()));
        existingOrder.setOrderStatus(OrderStatus.fromMappedValue(ordersDto.getOrderStatus()));
        existingOrder.setTaxAmount(ordersDto.getTaxAmount());
        existingOrder.setDiscountAmount(ordersDto.getDiscountAmount());
        existingOrder.setPaymentStatus(PaymentStatus.fromMappedValue(ordersDto.getPaymentStatus()));
        existingOrder.setPaymentMethod(PaymentMethod.fromMappedValue(ordersDto.getPaymentMethod()));
        existingOrder.setNotes(ordersDto.getNotes());
        existingOrder.setUpdatedAt(LocalDateTime.now());
        existingOrder.setUpdatedAt(LocalDateTime.now());

        if (ordersDto.getOrderItemsDtos() != null && !ordersDto.getOrderItemsDtos().isEmpty()) {
            Set<OrderItems> orderItems = ordersDto.getOrderItemsDtos().stream()
                    .map(this::convertForUpdate)
                    .collect(Collectors.toSet());
            existingOrder.setOrderItems(orderItems);
        }

        ordersRepository.save(existingOrder);

        webSocketChannelInterceptor.notifyKitchen();

        return new ResponseDto("Order updated successfully");
    }

    public OrderItems convertForUpdate(OrderItemsDto orderItemsDto) {
        if (orderItemsDto.getId() != null) {
            OrderItems existingOrderItem = orderItemsRepository.findById(orderItemsDto.getId())
                    .orElseThrow(() -> new ServiceException("Order item not found", "Bad request", HttpStatus.BAD_REQUEST));

            existingOrderItem.setMenuItemsId(orderItemsDto.getMenuItemsId());
            existingOrderItem.setQuantity(orderItemsDto.getQuantity());
            existingOrderItem.setSpecialInstructions(orderItemsDto.getSpecialInstructions());
            existingOrderItem.setUnitPrice(orderItemsDto.getUnitPrice());
            existingOrderItem.setTotalPrice(orderItemsDto.getTotalPrice());
            existingOrderItem.setStatus(OrderStatus.fromMappedValue(orderItemsDto.getStatus()));
            existingOrderItem.setUpdatedAt(LocalDateTime.now());
            existingOrderItem.setIsRetail(orderItemsDto.getIsRetail());

            return existingOrderItem;
        }else {
            return OrderItems.builder()
                    .id(orderItemsDto.getId())
                    .orderId(orderItemsDto.getOrderId())
                    .menuItemsId(orderItemsDto.getMenuItemsId())
                    .quantity(orderItemsDto.getQuantity())
                    .specialInstructions(orderItemsDto.getSpecialInstructions())
                    .createdAt(LocalDateTime.now())
                    .unitPrice(orderItemsDto.getUnitPrice())
                    .totalPrice(orderItemsDto.getTotalPrice())
                    .status(OrderStatus.PENDING)
                    .isRetail(orderItemsDto.getIsRetail())
                    .build();
        }
    }


    public ResponseDto updateOrderStatus(Long id, String status) {
        Orders orders = ordersRepository.findById(id).orElseThrow(() -> new
                ServiceException("Order not found", "Bad request", HttpStatus.BAD_REQUEST));

        if (status.equalsIgnoreCase(OrderStatus.CONFIRMED.getMappedValue())) {
            Set<OrderItems> orderItems = ordersRepository.getAllOrderItemsByOrderId(orders.getId());
            orderItems.stream().forEach(orderItemsDto -> {
                orderItemsDto.setStatus(OrderStatus.PREPARING);
            });
        }

        orders.setOrderStatus(OrderStatus.fromMappedValue(status));
        ordersRepository.save(orders);
        return new ResponseDto("Order status updated successfully");
    }

    public ResponseDto updatePaymentStatus(Long id, String status) {
        Orders orders = ordersRepository.findById(id).orElseThrow(() -> new
                ServiceException("Order not found", "Bad request", HttpStatus.BAD_REQUEST));

        orders.setPaymentStatus(PaymentStatus.fromMappedValue(status));
        ordersRepository.save(orders);
        return new ResponseDto("Payment status updated successfully");
    }

    public ResponseDto deleteOrder(Long id) {
        Orders orders = ordersRepository.findById(id).orElseThrow(() -> new
                ServiceException("Order not found", "Bad request", HttpStatus.BAD_REQUEST));
        ordersRepository.deleteById(id);
        return new ResponseDto("Order deleted successfully");
    }
}
