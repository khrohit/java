package com.shopsphere.labs.rmi;

import java.rmi.Naming;

public class InventoryRmiClient {
    public static void main(String[] args) throws Exception {
        InventoryService inventory = (InventoryService) Naming.lookup("rmi://127.0.0.1/ShopSphereInventory");
        long productId = args.length == 0 ? 1 : Long.parseLong(args[0]);
        System.out.println("Product: " + inventory.getProduct(productId));
        System.out.println("In stock: " + inventory.getStock(productId));
    }
}
