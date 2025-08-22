package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.CategoryDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.api.dto.TableDto;
import com.aura.syntax.pos.management.entity.Categories;
import com.aura.syntax.pos.management.entity.Tables;
import com.aura.syntax.pos.management.enums.Status;
import com.aura.syntax.pos.management.enums.TableStatus;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.TablesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TablesRepository tablesRepository;

    public ResponseDto saveTable(TableDto tableDto) {
        Tables tables = Tables.builder()
                .id(tableDto.getId())
                .tableNumber(tableDto.getTableNumber())
                .capacity(tableDto.getCapacity())
                .tableStatus(TableStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .build();
        tablesRepository.save(tables);
        return new ResponseDto("Table details created successfully");
    }

    public ResponseDto updateTable(TableDto tableDto){
        Tables existingTable = tablesRepository.findById(tableDto.getId())
                .orElseThrow(() -> new ServiceException("Table not found","Bad request", HttpStatus.BAD_REQUEST));

        existingTable.setId(tableDto.getId());
        existingTable.setTableNumber(tableDto.getTableNumber());
        existingTable.setTableStatus(tableDto.getTableStatus() != null ?
                TableStatus.fromMappedValue(tableDto.getTableStatus()) : null);
        existingTable.setCapacity(tableDto.getCapacity());
        existingTable.setUpdatedAt(LocalDateTime.now());
        tablesRepository.save(existingTable);
        return new ResponseDto("Table details updated successfully");

    }

    public TableDto getTableById(Long id){
        Tables table = tablesRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Table not found","Bad request", HttpStatus.BAD_REQUEST));

        return TableDto.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .tableStatus(table.getTableStatus().getMappedValue())
                .build();
    }

    public PaginatedResponseDto<TableDto> getAllTablesPagination(Integer page,Integer size, String search){
        Pageable pageable = PageRequest.of(page - 1,size);
        Page<TableDto> tableDtos = tablesRepository.getAllTablesPagination(pageable,search);
        PaginatedResponseDto<TableDto> tableDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<TableDto> tableDtosContent = tableDtos.getContent();
        tableDtoPaginatedResponseDto.setData(tableDtosContent);
        tableDtoPaginatedResponseDto.setCurrentPage(page);
        tableDtoPaginatedResponseDto.setTotalPages(tableDtos.getTotalPages());
        tableDtoPaginatedResponseDto.setTotalItems(tableDtos.getTotalElements());

        return tableDtoPaginatedResponseDto;
    }

    public List<TableDto> getListOfTables(String search, String status){
        return tablesRepository.getListOfTables(search,status != null ? TableStatus.fromMappedValue(status) : null);
    }

    public ResponseDto updateStatus(Long id, String status) {
        Tables table = tablesRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Table not found","Bad request", HttpStatus.BAD_REQUEST));

        table.setTableStatus(TableStatus.fromMappedValue(status));
        tablesRepository.save(table);
        return new ResponseDto("Table status updated successfully");

    }

    public ResponseDto deleteTable(Long id) {
        Tables table = tablesRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Table not found","Bad request", HttpStatus.BAD_REQUEST));
        tablesRepository.deleteById(id);
        return new ResponseDto("Table deleted successfully");

    }
}
