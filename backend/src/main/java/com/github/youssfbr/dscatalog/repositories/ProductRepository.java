package com.github.youssfbr.dscatalog.repositories;

import com.github.youssfbr.dscatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
