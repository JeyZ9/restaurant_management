package com.app.restaurant_management.repository;

import com.app.restaurant_management.models.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderITemRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> findAllByOrdersId(Long id);
}
