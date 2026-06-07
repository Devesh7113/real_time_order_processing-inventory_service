package com.real_time_order_processing.inventory.repository;

import com.real_time_order_processing.inventory.entity.HelpdeskTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpdeskRepository extends JpaRepository<HelpdeskTicket, Long>
{
    List<HelpdeskTicket> findByUserId(Long userId);
}
