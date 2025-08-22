package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.TableDto;
import com.aura.syntax.pos.management.entity.Tables;
import com.aura.syntax.pos.management.enums.TableStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TablesRepository extends JpaRepository<Tables, Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.TableDto(t.id,t.tableNumber,t.capacity,t.tableStatus) " +
           "FROM Tables t " +
           "WHERE :search IS NULL OR t.tableNumber = :search")
    Page<TableDto> getAllTablesPagination(Pageable pageable, String search);

    @Query("""
            SELECT NEW com.aura.syntax.pos.management.api.dto.TableDto(
                t.id, t.tableNumber, t.capacity, t.tableStatus
            )
            FROM Tables t
            WHERE (:search IS NULL OR t.tableNumber = :search)
              AND (:tableStatus IS NULL OR t.tableStatus = :tableStatus)
            """)
    List<TableDto> getListOfTables(String search, TableStatus tableStatus);

    @Query("SELECT t.tableNumber FROM Tables t WHERE t.id = :tableId")
    String getTableNameById(Long tableId);
}
