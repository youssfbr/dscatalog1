package com.github.youssfbr.dscatalog.services;

import com.github.youssfbr.dscatalog.dto.CategoryDTO;
import com.github.youssfbr.dscatalog.dto.ProductDTO;
import com.github.youssfbr.dscatalog.entities.Category;
import com.github.youssfbr.dscatalog.entities.Product;
import com.github.youssfbr.dscatalog.repositories.CategoryRepository;
import com.github.youssfbr.dscatalog.repositories.ProductRepository;
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
public class ProductService {
    public static final String RESOURCE_NOT_FOUND = "Resource not found with id ";
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository , CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest)
                .map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return productRepository.findById(id)
                .map(ProductDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND + id));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {

        final Product entity = new Product();
        copyDtoToEntity(dto , entity);

        final Product categorysaved = productRepository.save(entity);

        return new ProductDTO(categorysaved);
    }

    @Transactional
    public ProductDTO update(Long id , ProductDTO dto) {
        try {
            final Product entity = productRepository.getOne(id);
            copyDtoToEntity(dto , entity);

            final Product productUpdated = productRepository.save(entity);

            return new ProductDTO(productUpdated);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + id);
        }
    }

    private void copyDtoToEntity(ProductDTO dto , Product entity) {

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            final Category category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
