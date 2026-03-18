package com.lcwd.electronic.repositories;

import com.lcwd.electronic.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
