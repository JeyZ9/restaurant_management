package com.app.restaurant_management.repository;

import com.app.restaurant_management.commons.enums.RoleName;
import com.app.restaurant_management.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRoleName(RoleName name);
}
