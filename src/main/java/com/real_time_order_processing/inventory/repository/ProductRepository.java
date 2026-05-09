package com.real_time_order_processing.inventory.repository;

import com.real_time_order_processing.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    List<Product> findByIdIn(List<Long> ids);
}
