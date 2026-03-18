package com.lcwd.electronic.repositories;

import com.lcwd.electronic.dtos.OrderDto;
import com.lcwd.electronic.entities.Order;
import com.lcwd.electronic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);

}
