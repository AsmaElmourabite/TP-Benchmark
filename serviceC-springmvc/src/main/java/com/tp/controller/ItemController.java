package com.tp.controller;

import com.tp.entity.Category;
import com.tp.entity.Item;
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
@RequestMapping("/items")
public class ItemController {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllItems(
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Item> page;
        
        if (categoryId != null) {
            page = itemRepository.findByCategoryId(categoryId, pageable);
        } else {
            page = itemRepository.findAll(pageable);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", page.getContent());
        response.put("page", page.getNumber());
        response.put("size", page.getSize());
        response.put("total", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemRepository.findById(id);
        
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Item not found with id: " + id));
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody Item item) {
        // Vérifier si le SKU existe déjà
        if (item.getSku() != null && itemRepository.findBySku(item.getSku()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Item with SKU " + item.getSku() + " already exists"));
        }
        
        // Vérifier que la catégorie existe
        if (item.getCategory() == null || item.getCategory().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Category is required"));
        }
        
        Optional<Category> category = categoryRepository.findById(item.getCategory().getId());
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Category not found with id: " + item.getCategory().getId()));
        }
        
        item.setCategory(category.get());
        Item savedItem = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody Item item) {
        Optional<Item> existingItem = itemRepository.findById(id);
        
        if (existingItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Item not found with id: " + id));
        }
        
        // Vérifier si le SKU existe déjà pour un autre enregistrement
        if (item.getSku() != null && !item.getSku().equals(existingItem.get().getSku())) {
            Optional<Item> itemWithSku = itemRepository.findBySku(item.getSku());
            if (itemWithSku.isPresent() && !itemWithSku.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Item with SKU " + item.getSku() + " already exists"));
            }
        }
        
        // Vérifier que la catégorie existe si elle est fournie
        if (item.getCategory() != null && item.getCategory().getId() != null) {
            Optional<Category> category = categoryRepository.findById(item.getCategory().getId());
            if (category.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Category not found with id: " + item.getCategory().getId()));
            }
            item.setCategory(category.get());
        } else {
            item.setCategory(existingItem.get().getCategory());
        }
        
        item.setId(id);
        Item updatedItem = itemRepository.save(item);
        return ResponseEntity.ok(updatedItem);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        Optional<Item> item = itemRepository.findById(id);
        
        if (item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Item not found with id: " + id));
        }
        
        itemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

