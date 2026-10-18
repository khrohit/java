package com.shopsphere.labs.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class InventoryRmiServer {
    public static void main(String[] args) throws Exception {
        try { LocateRegistry.createRegistry(1099); }
        catch (java.rmi.server.ExportException alreadyRunning) { System.out.println("Using the existing RMI registry on port 1099."); }
        Naming.rebind("rmi://127.0.0.1/ShopSphereInventory", new InventoryServiceImpl());
        System.out.println("ShopSphere RMI inventory service is ready.");
    }
}
