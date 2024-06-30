package com.ali.customerService.repositories;

import com.ali.customerService.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findByFirstNameContainingIgnoreCase(String keyword);
    Optional<Customer> findByEmail(String email);
}