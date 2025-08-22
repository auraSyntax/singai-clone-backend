package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.OrdersDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.api.dto.SaveOrderResponseDto;
import com.aura.syntax.pos.management.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping
    public SaveOrderResponseDto saveOrder(@RequestBody OrdersDto ordersDto){
        return ordersService.addOrder(ordersDto);
    }

    @GetMapping("/get-by-id")
    public OrdersDto getOrderById(@RequestParam(value = "id") Long id){
        return ordersService.getOrderById(id);
    }

    @GetMapping("/get-all-pagination")
    public PaginatedResponseDto<OrdersDto> getAllOrdersPagination(@RequestParam(value = "page") Integer page,
                                                                 @RequestParam(value = "size") Integer size,
                                                                 @RequestParam(value = "waiterId",required = false) Long waiterId,
                                                                 @RequestParam(value = "orderType",required = false) String orderType,
                                                                 @RequestParam(value = "orderStatus",required = false) String orderStatus,
                                                                  @RequestParam(value = "search",required = false) String search){
        return ordersService.getAllOrdersPagination(page,size,waiterId,orderType,orderStatus,search);
    }

    @PutMapping
    public ResponseDto updateOrders(@RequestBody OrdersDto ordersDto){
        return ordersService.updateOrders(ordersDto);
    }

    @PutMapping("/order-status")
    public ResponseDto updateOrderStatus(@RequestParam(value = "id") Long id,
                                         @RequestParam(value = "status") String status){
        return ordersService.updateOrderStatus(id,status);
    }

    @PutMapping("/payment-status")
    public ResponseDto updatePaymentStatus(@RequestParam(value = "id") Long id,
                                         @RequestParam(value = "status") String status){
        return ordersService.updatePaymentStatus(id,status);
    }

    @DeleteMapping
    public ResponseDto deleteOrder(@RequestParam(value = "id") Long id){
        return ordersService.deleteOrder(id);
    }
}
