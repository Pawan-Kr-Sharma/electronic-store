package com.lcwd.electronic.dtos;

import com.lcwd.electronic.entities.CartItem;
import com.lcwd.electronic.entities.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private String cartItemId;
    private Date createdAt;
    private UserDto user;
    private List<CartItemDto> items = new ArrayList<>();
}
