package com.lcwd.electronic.repositories;

import com.lcwd.electronic.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
