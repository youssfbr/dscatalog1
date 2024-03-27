package com.github.youssfbr.dscatalog.services;

import com.github.youssfbr.dscatalog.dto.CategoryDTO;
import com.github.youssfbr.dscatalog.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }
}
