package com.tp.controller;

import com.tp.entity.Category;
import com.tp.repository.CategoryRepository;
import com.tp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Category> page = categoryRepository.findAll(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", page.getContent());
        response.put("page", page.getNumber());
        response.put("size", page.getSize());
        response.put("total", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Category not found with id: " + id));
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        // Vérifier si le code existe déjà
        if (category.getCode() != null && categoryRepository.findByCode(category.getCode()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Category with code " + category.getCode() + " already exists"));
        }
        
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        
        if (existingCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Category not found with id: " + id));
        }
        
        // Vérifier si le code existe déjà pour un autre enregistrement
        if (category.getCode() != null && !category.getCode().equals(existingCategory.get().getCode())) {
            Optional<Category> categoryWithCode = categoryRepository.findByCode(category.getCode());
            if (categoryWithCode.isPresent() && !categoryWithCode.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Category with code " + category.getCode() + " already exists"));
            }
        }
        
        category.setId(id);
        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Category not found with id: " + id));
        }
        
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/items")
    public ResponseEntity<?> getCategoryItems(
            @PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Optional<Category> category = categoryRepository.findById(id);
        
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Category not found with id: " + id));
        }
        
        Page<com.tp.entity.Item> page = itemRepository.findByCategoryId(id, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", page.getContent());
        response.put("page", page.getNumber());
        response.put("size", page.getSize());
        response.put("total", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
}

