package com.tp.dao;

import com.tp.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class CategoryDAO {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public List<Category> findAll(int page, int size) {
        TypedQuery<Category> query = entityManager.createQuery(
            "SELECT c FROM Category c ORDER BY c.id", Category.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
    
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM Category c", Long.class);
        return query.getSingleResult();
    }
    
    public Optional<Category> findById(Long id) {
        Category category = entityManager.find(Category.class, id);
        return Optional.ofNullable(category);
    }
    
    public Optional<Category> findByCode(String code) {
        TypedQuery<Category> query = entityManager.createQuery(
            "SELECT c FROM Category c WHERE c.code = :code", Category.class);
        query.setParameter("code", code);
        List<Category> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Transactional
    public Category save(Category category) {
        if (category.getId() == null) {
            entityManager.persist(category);
            return category;
        } else {
            return entityManager.merge(category);
        }
    }
    
    @Transactional
    public void delete(Long id) {
        Category category = entityManager.find(Category.class, id);
        if (category != null) {
            entityManager.remove(category);
        }
    }
}

