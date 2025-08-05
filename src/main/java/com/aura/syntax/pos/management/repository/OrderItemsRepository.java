package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems,Long> {
}
