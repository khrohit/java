package com.shopsphere.labs.swing;

import com.shopsphere.model.User;
import com.shopsphere.service.AuthService;
import javax.swing.*;
import java.awt.*;

public final class SwingAdminApp {
    private SwingAdminApp() { }
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) { }
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    private static final class LoginFrame extends JFrame {
        private final JTextField email = new JTextField(22);
        private final JPasswordField password = new JPasswordField(22);
        LoginFrame() {
            super("ShopSphere · Admin sign in"); setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JPanel form = new JPanel(new GridLayout(0, 1, 8, 8)); form.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
            form.add(new JLabel("Email")); form.add(email); form.add(new JLabel("Password")); form.add(password);
            JButton signIn = new JButton("Sign in"); signIn.addActionListener(event -> signIn()); form.add(signIn);
            add(form); pack(); setLocationRelativeTo(null);
        }
        private void signIn() {
            try {
                User user = new AuthService().login(email.getText(), new String(password.getPassword()));
                if (user == null || !user.isAdmin()) { JOptionPane.showMessageDialog(this, "Use a valid administrator account.", "Sign in failed", JOptionPane.ERROR_MESSAGE); return; }
                new InventoryFrame(user).setVisible(true); dispose();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Could not connect to the ShopSphere database. Check your DB configuration.", "Database unavailable", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static final class InventoryFrame extends JFrame {
        private final com.shopsphere.dao.ProductDAO products = new com.shopsphere.dao.ProductDAO();
        private final javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
                new Object[]{"ID", "Product", "Brand", "Price (INR)", "Stock"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        InventoryFrame(User user) {
            super("ShopSphere · Inventory · " + user.getName()); setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JTable table = new JTable(model); table.setRowHeight(28); add(new JScrollPane(table), BorderLayout.CENTER);
            JButton refresh = new JButton("Refresh"); refresh.addActionListener(event -> loadProducts());
            JButton stock = new JButton("Set stock"); stock.addActionListener(event -> setStock(table));
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.add(refresh); actions.add(stock); add(actions, BorderLayout.SOUTH);
            setSize(780, 480); setLocationRelativeTo(null); loadProducts();
        }
        private void loadProducts() {
            try {
                model.setRowCount(0);
                for (var product : products.findAllForAdmin()) model.addRow(new Object[]{product.getProductId(), product.getName(), product.getBrand(), product.getSalePrice(), product.getStock()});
            } catch (Exception exception) { JOptionPane.showMessageDialog(this, "Products could not be loaded.", "Database error", JOptionPane.ERROR_MESSAGE); }
        }
        private void setStock(JTable table) {
            int row = table.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Select a product first."); return; }
            String value = JOptionPane.showInputDialog(this, "New stock quantity", model.getValueAt(row, 4));
            if (value == null) return;
            try {
                int quantity = Integer.parseInt(value);
                if (!products.setStock(((Number) model.getValueAt(row, 0)).longValue(), quantity)) throw new IllegalArgumentException();
                loadProducts();
            } catch (Exception exception) { JOptionPane.showMessageDialog(this, "Enter a valid stock value.", "Update failed", JOptionPane.ERROR_MESSAGE); }
        }
    }
}
