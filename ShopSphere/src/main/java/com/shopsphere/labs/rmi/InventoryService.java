package com.shopsphere.labs.rmi;

import com.shopsphere.labs.network.NetworkProduct;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InventoryService extends Remote {
    NetworkProduct getProduct(long productId) throws RemoteException;
    int getStock(long productId) throws RemoteException;
    boolean updateStock(long productId, int stock) throws RemoteException;
    List<NetworkProduct> search(String term) throws RemoteException;
}
