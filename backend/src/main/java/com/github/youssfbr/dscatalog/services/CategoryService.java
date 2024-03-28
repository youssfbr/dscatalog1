package com.github.youssfbr.dscatalog.services;

import com.github.youssfbr.dscatalog.dto.CategoryDTO;
import com.github.youssfbr.dscatalog.entities.Category;
import com.github.youssfbr.dscatalog.repositories.CategoryRepository;
import com.github.youssfbr.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    public static final String RESOURCE_NOT_FOUND = "Resource not found with id ";
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

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND + id));
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {

        final Category categoryToSave = new Category();
        categoryToSave.setName(dto.getName());

        final Category categorysaved = categoryRepository.save(categoryToSave);

        return new CategoryDTO(categorysaved);
    }

    @Transactional
    public CategoryDTO update(Long id , CategoryDTO dto) {
        try {
            final Category categoryToSave = categoryRepository.getOne(id);
            categoryToSave.setName(dto.getName());

            final Category categorySaved = categoryRepository.save(categoryToSave);

            return new CategoryDTO(categorySaved);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + id);
        }
    }
}
