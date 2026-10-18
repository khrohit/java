package com.shopsphere.service;

import com.shopsphere.dao.AdminDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.OrderSummary;
import com.shopsphere.model.Product;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdminService {
    private final AdminDAO admin = new AdminDAO();
    private final ProductDAO products = new ProductDAO();
    private final OrderDAO orders = new OrderDAO();
    public Map<String, Long> counts() throws SQLException { return admin.dashboardCounts(); }
    public List<Product> products() throws SQLException { return products.findAllForAdmin(); }
    public List<String[]> categories() throws SQLException { return products.findCategories(); }
    public void save(Product product) throws SQLException { products.save(product); }
    public void archive(long productId) throws SQLException { products.archive(productId); }
    public List<OrderSummary> orders() throws SQLException { return orders.findAll(); }
    public void updateOrder(long orderId, String status) throws SQLException { orders.updateStatus(orderId, status); }
}
