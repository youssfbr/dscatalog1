package com.github.youssfbr.dscatalog.resources;

import com.github.youssfbr.dscatalog.dto.CategoryDTO;
import com.github.youssfbr.dscatalog.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {
    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<CategoryDTO>> findAllPaged(
            @RequestParam(value = "page" , defaultValue = "0") Integer page ,
            @RequestParam(value = "linesPerPage" , defaultValue = "12") Integer linesPerPage ,
            @RequestParam(value = "direction" , defaultValue = "ASC") String direction ,
            @RequestParam(value = "orderBy" , defaultValue = "name") String orderBy
    ) {
        final PageRequest pageRequest = PageRequest
                .of(page , linesPerPage , Sort.Direction.valueOf(direction) , orderBy);

        return ResponseEntity.ok(categoryService.findAllPaged(pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {

        final CategoryDTO categoryCreated = categoryService.insert(dto);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(categoryCreated.getId()).toUri();

        return ResponseEntity.created(location).body(categoryCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id , @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.update(id , dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
