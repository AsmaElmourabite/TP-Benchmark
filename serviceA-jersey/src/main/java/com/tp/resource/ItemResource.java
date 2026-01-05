package com.tp.resource;

import com.tp.dao.CategoryDAO;
import com.tp.dao.ItemDAO;
import com.tp.entity.Category;
import com.tp.entity.Item;
import com.tp.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {
    
    private EntityManager getEntityManager() {
        return EntityManagerUtil.getEntityManager();
    }
    
    private ItemDAO getItemDAO() {
        ItemDAO dao = new ItemDAO();
        dao.setEntityManager(getEntityManager());
        return dao;
    }
    
    private CategoryDAO getCategoryDAO() {
        CategoryDAO dao = new CategoryDAO();
        dao.setEntityManager(getEntityManager());
        return dao;
    }
    
    @GET
    public Response getAllItems(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("categoryId") Long categoryId) {
        
        ItemDAO dao = getItemDAO();
        List<Item> items;
        long total;
        
        if (categoryId != null) {
            items = dao.findByCategoryId(categoryId, page, size);
            total = dao.countByCategoryId(categoryId);
        } else {
            items = dao.findAll(page, size);
            total = dao.count();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", items);
        response.put("page", page);
        response.put("size", size);
        response.put("total", total);
        response.put("totalPages", (total + size - 1) / size);
        
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") Long id) {
        ItemDAO dao = getItemDAO();
        Optional<Item> item = dao.findById(id);
        
        if (item.isPresent()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Item not found with id: " + id))
                    .build();
        }
    }
    
    @POST
    public Response createItem(Item item) {
        ItemDAO itemDAO = getItemDAO();
        CategoryDAO categoryDAO = getCategoryDAO();
        
        // Vérifier si le SKU existe déjà
        if (item.getSku() != null && itemDAO.findBySku(item.getSku()).isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Item with SKU " + item.getSku() + " already exists"))
                    .build();
        }
        
        // Vérifier que la catégorie existe
        if (item.getCategory() == null || item.getCategory().getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Category is required"))
                    .build();
        }
        
        Optional<Category> category = categoryDAO.findById(item.getCategory().getId());
        if (category.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Category not found with id: " + item.getCategory().getId()))
                    .build();
        }
        
        item.setCategory(category.get());
        Item savedItem = itemDAO.save(item);
        return Response.status(Response.Status.CREATED).entity(savedItem).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateItem(@PathParam("id") Long id, Item item) {
        ItemDAO itemDAO = getItemDAO();
        CategoryDAO categoryDAO = getCategoryDAO();
        Optional<Item> existingItem = itemDAO.findById(id);
        
        if (existingItem.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Item not found with id: " + id))
                    .build();
        }
        
        // Vérifier si le SKU existe déjà pour un autre enregistrement
        if (item.getSku() != null && !item.getSku().equals(existingItem.get().getSku())) {
            Optional<Item> itemWithSku = itemDAO.findBySku(item.getSku());
            if (itemWithSku.isPresent() && !itemWithSku.get().getId().equals(id)) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(Map.of("error", "Item with SKU " + item.getSku() + " already exists"))
                        .build();
            }
        }
        
        // Vérifier que la catégorie existe si elle est fournie
        if (item.getCategory() != null && item.getCategory().getId() != null) {
            Optional<Category> category = categoryDAO.findById(item.getCategory().getId());
            if (category.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Category not found with id: " + item.getCategory().getId()))
                        .build();
            }
            item.setCategory(category.get());
        } else {
            item.setCategory(existingItem.get().getCategory());
        }
        
        item.setId(id);
        Item updatedItem = itemDAO.save(item);
        return Response.ok(updatedItem).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        ItemDAO dao = getItemDAO();
        Optional<Item> item = dao.findById(id);
        
        if (item.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Item not found with id: " + id))
                    .build();
        }
        
        dao.delete(id);
        return Response.noContent().build();
    }
}

