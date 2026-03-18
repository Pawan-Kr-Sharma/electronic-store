package com.lcwd.electronic.dtos;

import com.lcwd.electronic.entities.Cart;
import com.lcwd.electronic.entities.Product;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private int cartItemId;

    private ProductDto product;

    private int quantity;
    private int totalPrice;


    //we don't need cart because we fetch cart items
    //private CartDto cart;

}
