package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.OrderItemsDto;
import com.aura.syntax.pos.management.api.dto.OrdersDto;
import com.aura.syntax.pos.management.config.websocket.OrdersWebSocketDto;
import com.aura.syntax.pos.management.entity.OrderItems;
import com.aura.syntax.pos.management.entity.Orders;
import com.aura.syntax.pos.management.enums.OrderStatus;
import com.aura.syntax.pos.management.enums.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface OrdersRepository extends JpaRepository<Orders,Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrdersDto(o.id, o.orderNumber, o.tableId, o.waiterId, o.orderType, o.orderStatus, o.paymentMethod, o.paymentStatus, o.customerName, o.customerPhone, o.notes,o.subTotal,o.discountAmount,o.taxAmount,t.tableNumber, u.lastName AS waiterName,o.createdAt,o.updatedAt) " +
           "FROM Orders o " +
           "LEFT JOIN Tables t ON o.tableId = t.id " +
           "LEFT JOIN User u ON o.waiterId = u.id " +
           "WHERE (:waiterId IS NULL OR o.waiterId = :waiterId) " +
           "AND (:orderType IS NULL OR o.orderType = :orderType) " +
           "AND (:orderStatus IS NULL OR o.orderStatus = :orderStatus) " +
           "AND (:search IS NULL OR (" +
           "    LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "    LOWER(o.customerName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "    LOWER(o.customerPhone) LIKE LOWER(CONCAT('%', :search, '%'))" +
           "))" +
           "ORDER BY o.createdAt DESC")
    Page<OrdersDto> getAllOrdersPagination(Pageable pageable, Long waiterId, OrderType orderType, OrderStatus orderStatus,String search);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrdersDto(o.id, o.orderNumber, o.tableId, o.waiterId, o.orderType, o.orderStatus, o.paymentMethod, o.paymentStatus, o.customerName, o.customerPhone, o.notes,o.subTotal,o.discountAmount,o.taxAmount,t.tableNumber, u.lastName AS waiterName,o.createdAt,o.updatedAt) " +
           "FROM Orders o " +
           "LEFT JOIN Tables t ON o.tableId = t.id " +
           "LEFT JOIN User u ON o.waiterId = u.id " +
           "WHERE o.orderStatus = 'CONFIRMED' " +
           "ORDER BY o.updatedAt DESC LIMIT 1")
    OrdersDto getOrderForSocket();

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrderItemsDto(oi.id,oi.menuItemsId,oi.quantity,oi.specialInstructions,oi.status,m.name, " +
           "oi.unitPrice, oi.totalPrice,oi.createdAt,oi.updatedAt,m.price,m.imageUrl,m.preparationTime,oi.isRetail) " +
           "FROM OrderItems oi " +
           "LEFT JOIN MenuItems m ON oi.menuItemsId = m.id " +
           "WHERE oi.orderId = :id " +
           "ORDER BY oi.createdAt DESC")
    Set<OrderItemsDto> getOrderItemsUnderOrderForSocket(Long id);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrderItemsDto(oi.id,oi.menuItemsId,oi.quantity,oi.specialInstructions,oi.status,m.name, " +
           "oi.unitPrice, oi.totalPrice,oi.createdAt,oi.updatedAt,m.price,m.imageUrl,m.preparationTime,oi.isRetail) " +
           "FROM OrderItems oi " +
           "LEFT JOIN MenuItems m ON oi.menuItemsId = m.id " +
           "WHERE oi.orderId = :id " +
           "AND :search IS NULL OR m.name LIKE LOWER(CONCAT('%',:search,'%')) " +
           "ORDER BY oi.createdAt DESC")
    Set<OrderItemsDto> getAllOrderItems(Long id,String search);

    @Query("SELECT oi " +
           "FROM OrderItems oi " +
           "WHERE oi.orderId = :id")
    Set<OrderItems> getAllOrderItemsByOrderId(Long id);

    @Query("""
            SELECT NEW com.aura.syntax.pos.management.config.websocket.OrdersWebSocketDto(o.id,o.orderNumber,o.orderType,o.orderStatus)
            FROM Orders o
            WHERE o.createdAt BETWEEN :startOfDay AND :endOfDay
            AND o.orderStatus = 'CONFIRMED'
            """)
    List<OrdersWebSocketDto> getAllOrdersForKitchen(LocalDateTime startOfDay,LocalDateTime endOfDay);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrdersDto(o.id,o.subTotal,o.discountAmount,o.taxAmount,o.paymentMethod) " +
           "FROM Orders o " +
           "WHERE DATE(o.updatedAt) BETWEEN :startDate AND :endDate " +
           "AND o.paymentStatus = 'PAID' " +
           "ORDER BY o.createdAt DESC")
    List<OrdersDto> getAllOrders(LocalDate startDate, LocalDate endDate);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrderItemsDto(oi.id,oi.quantity, " +
           "oi.unitPrice) " +
           "FROM OrderItems oi " +
           "LEFT JOIN MenuItems m ON oi.menuItemsId = m.id " +
           "WHERE oi.orderId = :id " +
           "ORDER BY oi.createdAt DESC")
    List<OrderItemsDto> getAllOrderItemsByOrder(Long id);
}
