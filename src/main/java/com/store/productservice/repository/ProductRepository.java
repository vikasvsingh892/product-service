package com.store.productservice.repository;

import com.store.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Search by Category (Filtering)
    List<Product> findByCategory(String category);

    // Search by Name (Filtering with Contains)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Pagination & Sorting
    Page<Product> findAll(Pageable pageable);
}
