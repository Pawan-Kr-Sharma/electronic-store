package com.lcwd.electronic.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderItemDto {
    private int orderItemId;

    private int quantity;
    private int totalPrice;

    private ProductDto product;


//    private OrderDto order;    //orderitem ke sath order bhejne ki koi jrurat nhi
}
