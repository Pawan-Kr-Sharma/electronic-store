package com.lcwd.electronic.services;

import com.lcwd.electronic.dtos.*;

import java.util.List;


public interface OrderService {

    //create order
    //OrderDto createOrder(OrderDto orderDto, String userId,String cartId);

    //if chenge the orderRequestClass in parameters
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove order
    void removeOrder(String orderId);

    //get order of user
    List<OrderDto> getOrderOfUser(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    //other method(logic) related to the order

    //update order and payment status
    OrderDto updatePaymentAndOrderStatus(String orderId, UpdatePaymentAndOrderStatusRequest request);
}
