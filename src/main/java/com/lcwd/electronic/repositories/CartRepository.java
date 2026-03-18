package com.lcwd.electronic.repositories;

import com.lcwd.electronic.entities.Cart;
import com.lcwd.electronic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUser(User user);
}
