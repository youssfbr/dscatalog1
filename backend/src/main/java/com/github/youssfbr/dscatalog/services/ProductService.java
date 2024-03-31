package com.github.youssfbr.dscatalog.services;

import com.github.youssfbr.dscatalog.dto.ProductDTO;
import com.github.youssfbr.dscatalog.entities.Product;
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

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

        final Product productToSave = new Product();
        productToSave.setName(dto.getName());

        final Product categorysaved = productRepository.save(productToSave);

        return new ProductDTO(categorysaved);
    }

    @Transactional
    public ProductDTO update(Long id , ProductDTO dto) {
        try {
            final Product productToSave = productRepository.getOne(id);
            productToSave.setName(dto.getName());

            final Product productSaved = productRepository.save(productToSave);

            return new ProductDTO(productSaved);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND + id);
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
