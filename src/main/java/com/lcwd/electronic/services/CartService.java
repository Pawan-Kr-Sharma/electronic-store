package com.lcwd.electronic.services;

import com.lcwd.electronic.dtos.AddItemToCartRequest;
import com.lcwd.electronic.dtos.CartDto;

public interface CartService {
    //add item to cart
    //case1: cart for user not available : we will create the cart and then add the item
    //case2: cart is available add the items to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId,int cartItem);

    //remove all items from cart
    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}
