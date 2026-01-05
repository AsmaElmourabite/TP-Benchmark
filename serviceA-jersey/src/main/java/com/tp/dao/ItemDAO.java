package com.tp.dao;

import com.tp.entity.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class ItemDAO {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public List<Item> findAll(int page, int size) {
        TypedQuery<Item> query = entityManager.createQuery(
            "SELECT i FROM Item i ORDER BY i.id", Item.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
    
    public List<Item> findByCategoryId(Long categoryId, int page, int size) {
        TypedQuery<Item> query = entityManager.createQuery(
            "SELECT i FROM Item i WHERE i.category.id = :categoryId ORDER BY i.id", Item.class);
        query.setParameter("categoryId", categoryId);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
    
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(i) FROM Item i", Long.class);
        return query.getSingleResult();
    }
    
    public long countByCategoryId(Long categoryId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(i) FROM Item i WHERE i.category.id = :categoryId", Long.class);
        query.setParameter("categoryId", categoryId);
        return query.getSingleResult();
    }
    
    public Optional<Item> findById(Long id) {
        Item item = entityManager.find(Item.class, id);
        return Optional.ofNullable(item);
    }
    
    public Optional<Item> findBySku(String sku) {
        TypedQuery<Item> query = entityManager.createQuery(
            "SELECT i FROM Item i WHERE i.sku = :sku", Item.class);
        query.setParameter("sku", sku);
        List<Item> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Transactional
    public Item save(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);
            return item;
        } else {
            return entityManager.merge(item);
        }
    }
    
    @Transactional
    public void delete(Long id) {
        Item item = entityManager.find(Item.class, id);
        if (item != null) {
            entityManager.remove(item);
        }
    }
}

