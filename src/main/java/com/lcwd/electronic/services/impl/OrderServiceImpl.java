package com.lcwd.electronic.services.impl;

import com.lcwd.electronic.dtos.*;
import com.lcwd.electronic.entities.*;
import com.lcwd.electronic.exceptions.BadApiRequestException;
import com.lcwd.electronic.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.helper.Helper;
import com.lcwd.electronic.repositories.CartRepository;
import com.lcwd.electronic.repositories.OrderRepository;
import com.lcwd.electronic.repositories.UserRepository;
import com.lcwd.electronic.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with given id !!"));
        //fetch cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart is not found of given id in server"));
        //to get cart of cartItems
        List<CartItem> cartItems = cart.getItems();

        if (cartItems.size() <= 0) {
            throw new BadApiRequestException("Invalid number of item in cart !!");
        }
        //other checks

        //if all fine then generate a order
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .billingName(orderDto.getBillingName())
                .billingAddress(orderDto.getBillingAddress())
                .billingPhone(orderDto.getBillingPhone())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .user(user)
                .build();

        //orderItems , amount not set
        //orderItems banana pdega kaise saare cartitems ko convert krnege orderItems mai
        //all cartItems ko convert(traverse) krenge orderItems mai
        //orderAmount ke liye order amount ko add krte jaayenge
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());

            return orderItem;
        }).collect(Collectors.toList());

        //set order items
        order.setOrderItems(orderItems);
        //set order amount
        order.setOrderAmount(orderAmount.get());

        //clear cart & save
        cart.getItems().clear();
        cartRepository.save(cart);

        //save order
        Order savedOrder = orderRepository.save(order);
        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order is not found !!"));
        orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getOrderOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found !!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Helper.getPageableResponse(page, OrderDto.class);
    }

    @Override
    public OrderDto updatePaymentAndOrderStatus(String orderId, UpdatePaymentAndOrderStatusRequest request) {
        //String paymentStatus = request.getPaymentStatus();
        //String orderStatus = request.getOrderStatus();

        //fetch order
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found !!"));
        //set
        order.setPaymentStatus(request.getPaymentStatus());
        order.setOrderStatus(request.getOrderStatus());
        order.setDeliveredDate(new Date());

        Order updatedOrder = orderRepository.save(order);

        return mapper.map(updatedOrder, OrderDto.class);
    }

}
