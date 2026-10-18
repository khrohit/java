package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import java.sql.SQLException;
import java.util.List;

public class CatalogService {
    private final ProductDAO products = new ProductDAO();
    public List<Product> featured() throws SQLException { return products.findFeatured(); }
    public List<Product> search(String q, Long category, String sort) throws SQLException { return products.findAll(q, category, sort); }
    public Product find(long id) throws SQLException { return products.findById(id); }
    public List<String[]> categories() throws SQLException { return products.findCategories(); }
}
