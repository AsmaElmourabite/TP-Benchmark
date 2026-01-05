package com.tp.repository;

import com.tp.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    Optional<Item> findBySku(String sku);
    
    Page<Item> findAll(Pageable pageable);
    
    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);
    
    @Query("SELECT COUNT(i) FROM Item i WHERE i.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);
}

