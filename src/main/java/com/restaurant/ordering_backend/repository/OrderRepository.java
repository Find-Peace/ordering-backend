package com.restaurant.ordering_backend.repository;

import com.restaurant.ordering_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
