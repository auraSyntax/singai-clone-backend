package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.api.dto.TableDto;
import com.aura.syntax.pos.management.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/table")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class TableController {

    private final TableService tableService;

    @PostMapping
    public ResponseDto saveTable(@RequestBody TableDto tableDto){
        return tableService.saveTable(tableDto);
    }

    @GetMapping("/list")
    public List<TableDto> getListOfTables(@RequestParam(value = "search",required = false) String search){
        return tableService.getListOfTables(search);
    }

    @GetMapping("/get-all")
    public PaginatedResponseDto<TableDto> getAllTablesPagination(@RequestParam(value = "page") Integer page,
                                                                        @RequestParam(value = "size") Integer size,
                                                                        @RequestParam(value = "search",required = false) String search){
        return tableService.getAllTablesPagination(page,size,search);
    }

    @GetMapping("/get-by-id")
    public TableDto getTableById(@RequestParam(value = "id") Long id){
        return tableService.getTableById(id);
    }

    @PutMapping
    public ResponseDto updateTable(@RequestBody TableDto tableDto){
        return tableService.updateTable(tableDto);
    }

    @PutMapping("/update-status")
    public ResponseDto updateStatus(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "status") String status){
        return tableService.updateStatus(id,status);
    }

    @DeleteMapping
    public ResponseDto deleteTable(@RequestParam(value = "id") Long id){
        return tableService.deleteTable(id);
    }
}
