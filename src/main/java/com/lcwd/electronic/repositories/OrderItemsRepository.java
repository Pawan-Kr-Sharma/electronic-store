package com.lcwd.electronic.repositories;

import com.lcwd.electronic.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Integer> {

}
