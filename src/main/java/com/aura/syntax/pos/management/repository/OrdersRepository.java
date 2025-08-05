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

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrdersDto(o.id,o.orderNumber,o.tableId,o.waiterId,o.orderType,o.orderStatus,o.paymentMethod,o.paymentStatus,o.customerName,o.customerPhone,o.notes) " +
           "FROM Orders o " +
           "WHERE :waiterId IS NULL OR o.waiterId = :waiterId " +
           "AND :orderType IS NULL OR o.orderType = :orderType " +
           "AND :orderStatus IS NULL OR o.orderStatus = :orderStatus")
    Page<OrdersDto> getAllOrdersPagination(Pageable pageable, Long waiterId, OrderType orderType, OrderStatus orderStatus);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.OrderItemsDto(oi.id,oi.menuItemsId,oi.quantity,oi.specialInstructions,oi.status) " +
           "FROM OrderItems oi " +
           "WHERE oi.orderId = :id")
    Set<OrderItemsDto> getAllOrderItems(Long id);

    @Query("SELECT oi " +
           "FROM OrderItems oi " +
           "WHERE oi.orderId = :id")
    Set<OrderItems> getAllOrderItemsByOrderId(Long id);
}
