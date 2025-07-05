package com.aura.syntax.pos.management.entity;

import com.aura.syntax.pos.management.enums.Status;
import com.aura.syntax.pos.management.enums.TableStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RestaurantTables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tableNumber;
    private Integer capacity;
    @Enumerated(EnumType.STRING)
    private TableStatus tableStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
