package com.example.shopapp.repository;

import com.example.shopapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable); // phân trang
}
