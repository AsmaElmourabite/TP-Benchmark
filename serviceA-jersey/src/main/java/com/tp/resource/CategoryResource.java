package com.tp.resource;

import com.tp.dao.CategoryDAO;
import com.tp.entity.Category;
import com.tp.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {
    
    private EntityManager getEntityManager() {
        return EntityManagerUtil.getEntityManager();
    }
    
    private CategoryDAO getCategoryDAO() {
        CategoryDAO dao = new CategoryDAO();
        dao.setEntityManager(getEntityManager());
        return dao;
    }
    
    @GET
    public Response getAllCategories(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        
        CategoryDAO dao = getCategoryDAO();
        List<Category> categories = dao.findAll(page, size);
        long total = dao.count();
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", categories);
        response.put("page", page);
        response.put("size", size);
        response.put("total", total);
        response.put("totalPages", (total + size - 1) / size);
        
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        CategoryDAO dao = getCategoryDAO();
        Optional<Category> category = dao.findById(id);
        
        if (category.isPresent()) {
            return Response.ok(category.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Category not found with id: " + id))
                    .build();
        }
    }
    
    @POST
    public Response createCategory(Category category) {
        CategoryDAO dao = getCategoryDAO();
        
        // Vérifier si le code existe déjà
        if (category.getCode() != null && dao.findByCode(category.getCode()).isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Category with code " + category.getCode() + " already exists"))
                    .build();
        }
        
        Category savedCategory = dao.save(category);
        return Response.status(Response.Status.CREATED).entity(savedCategory).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, Category category) {
        CategoryDAO dao = getCategoryDAO();
        Optional<Category> existingCategory = dao.findById(id);
        
        if (existingCategory.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Category not found with id: " + id))
                    .build();
        }
        
        // Vérifier si le code existe déjà pour un autre enregistrement
        if (category.getCode() != null && !category.getCode().equals(existingCategory.get().getCode())) {
            Optional<Category> categoryWithCode = dao.findByCode(category.getCode());
            if (categoryWithCode.isPresent() && !categoryWithCode.get().getId().equals(id)) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(Map.of("error", "Category with code " + category.getCode() + " already exists"))
                        .build();
            }
        }
        
        category.setId(id);
        Category updatedCategory = dao.save(category);
        return Response.ok(updatedCategory).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        CategoryDAO dao = getCategoryDAO();
        Optional<Category> category = dao.findById(id);
        
        if (category.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Category not found with id: " + id))
                    .build();
        }
        
        dao.delete(id);
        return Response.noContent().build();
    }
    
    @GET
    @Path("/{id}/items")
    public Response getCategoryItems(
            @PathParam("id") Long id,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        
        CategoryDAO categoryDAO = getCategoryDAO();
        Optional<Category> category = categoryDAO.findById(id);
        
        if (category.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Category not found with id: " + id))
                    .build();
        }
        
        com.tp.dao.ItemDAO itemDAO = new com.tp.dao.ItemDAO();
        itemDAO.setEntityManager(getEntityManager());
        
        List<com.tp.entity.Item> items = itemDAO.findByCategoryId(id, page, size);
        long total = itemDAO.countByCategoryId(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", items);
        response.put("page", page);
        response.put("size", size);
        response.put("total", total);
        response.put("totalPages", (total + size - 1) / size);
        
        return Response.ok(response).build();
    }
}

