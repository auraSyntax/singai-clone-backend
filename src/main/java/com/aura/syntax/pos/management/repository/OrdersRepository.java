package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.OrderItemsDto;
import com.aura.syntax.pos.management.api.dto.OrdersDto;
import com.aura.syntax.pos.management.entity.OrderItems;
import com.aura.syntax.pos.management.entity.Orders;
import com.aura.syntax.pos.management.enums.OrderStatus;
import com.aura.syntax.pos.management.enums.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface OrdersRepository extends JpaRepository<Orders,Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrdersDto(o.id, o.orderNumber, o.tableId, o.waiterId, o.orderType, o.orderStatus, o.paymentMethod, o.paymentStatus, o.customerName, o.customerPhone, o.notes,o.subTotal,o.discountAmount,o.taxAmount,t.tableNumber, CONCAT(u.firstName,' ',u.lastName) AS waiterName) " +
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
           "))")
    Page<OrdersDto> getAllOrdersPagination(Pageable pageable, Long waiterId, OrderType orderType, OrderStatus orderStatus,String search);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrderItemsDto(oi.id,oi.menuItemsId,oi.quantity,oi.specialInstructions,oi.status,m.name, " +
           "oi.unitPrice, oi.totalPrice,oi.createdAt,oi.updatedAt,m.price,m.imageUrl,m.preparationTime,oi.isRetail) " +
           "FROM OrderItems oi " +
           "LEFT JOIN MenuItems m ON oi.menuItemsId = m.id " +
           "WHERE oi.orderId = :id")
    Set<OrderItemsDto> getAllOrderItems(Long id);

    @Query("SELECT oi " +
           "FROM OrderItems oi " +
           "WHERE oi.orderId = :id")
    Set<OrderItems> getAllOrderItemsByOrderId(Long id);
}
