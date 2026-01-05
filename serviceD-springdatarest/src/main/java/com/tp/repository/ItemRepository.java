package com.tp.repository;

import com.tp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "items", path = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    @RestResource(path = "findByCategoryId", rel = "findByCategoryId")
    List<Item> findByCategoryId(@Param("categoryId") Long categoryId);
}

