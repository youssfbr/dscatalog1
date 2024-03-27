package com.github.youssfbr.dscatalog.repositories;

import com.github.youssfbr.dscatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
