package com.shopsphere.labs.network;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class InventorySocketServer {
    private static final int PORT = 5055;
    private final ProductDAO products = new ProductDAO();

    public static void main(String[] args) throws Exception {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("ShopSphere inventory socket server listening on " + PORT);
            while (true) {
                Socket socket = server.accept();
                new Thread(() -> handle(socket), "shopsphere-inventory-client").start();
            }
        }
    }

    private static void handle(Socket socket) {
        try (socket;
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            InventoryRequest request = (InventoryRequest) in.readObject();
            InventorySocketServer service = new InventorySocketServer();
            List<NetworkProduct> result = switch (request.command()) {
                case "GET_PRODUCT" -> {
                    Product p = service.products.findById(request.productId());
                    yield p == null ? List.of() : List.of(map(p));
                }
                case "GET_STOCK" -> {
                    Product p = service.products.findById(request.productId());
                    yield p == null ? List.of() : List.of(map(p));
                }
                case "SEARCH_PRODUCT" -> service.products.findAll(request.searchTerm(), null, "name").stream().map(InventorySocketServer::map).toList();
                default -> List.of();
            };
            out.writeObject(new InventoryResponse(result.isEmpty() ? "No matching products." : "Inventory response ready.", result));
            out.flush();
        } catch (Exception exception) {
            System.err.println("Inventory request failed: " + exception.getMessage());
        }
    }

    private static NetworkProduct map(Product p) { return new NetworkProduct(p.getProductId(), p.getName(), p.getSalePrice(), p.getStock()); }
}
