package com.github.youssfbr.dscatalog.services;

import com.github.youssfbr.dscatalog.dto.CategoryDTO;
import com.github.youssfbr.dscatalog.entities.Category;
import com.github.youssfbr.dscatalog.repositories.CategoryRepository;
import com.github.youssfbr.dscatalog.services.exceptions.DatabaseException;
import com.github.youssfbr.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest)
                .map(CategoryDTO::new);
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

            final Category categoryUpdated = categoryRepository.save(categoryToSave);

            return new CategoryDTO(categoryUpdated);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + id);
        }
    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
