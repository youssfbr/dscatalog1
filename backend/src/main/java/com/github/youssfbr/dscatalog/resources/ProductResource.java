package com.github.youssfbr.dscatalog.resources;

import com.github.youssfbr.dscatalog.dto.ProductDTO;
import com.github.youssfbr.dscatalog.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductResource {
    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ProductDTO>> findAllPaged(
            @RequestParam(value = "page" , defaultValue = "0") Integer page ,
            @RequestParam(value = "linesPerPage" , defaultValue = "12") Integer linesPerPage ,
            @RequestParam(value = "direction" , defaultValue = "ASC") String direction ,
            @RequestParam(value = "orderBy" , defaultValue = "name") String orderBy
    ) {
        final PageRequest pageRequest = PageRequest
                .of(page , linesPerPage , Sort.Direction.valueOf(direction) , orderBy);

        return ResponseEntity.ok(productService.findAllPaged(pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto) {

        final ProductDTO productCreated = productService.insert(dto);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(productCreated.getId()).toUri();

        return ResponseEntity.created(location).body(productCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id , @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.update(id , dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
