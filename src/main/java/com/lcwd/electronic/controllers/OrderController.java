package com.lcwd.electronic.controllers;

import com.lcwd.electronic.dtos.*;
import com.lcwd.electronic.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);

        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("order is removed !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
        List<OrderDto> orders = orderService.getOrderOfUser(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PageableResponse<OrderDto>> getOrdersOfUser(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "orderedDate",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir
    )
    {
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    //update payment & order status
    @PutMapping("/orderStatus/{orderId}")
    public ResponseEntity<OrderDto> updatePaymentAndOrderStatus(@PathVariable String orderId,
                                                                @RequestBody UpdatePaymentAndOrderStatusRequest request
                                                                ){
        OrderDto status = orderService.updatePaymentAndOrderStatus(orderId, request);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }
}
