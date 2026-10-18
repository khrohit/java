package com.shopsphere.labs.rmi;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.labs.network.NetworkProduct;
import com.shopsphere.model.Product;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {
    private final ProductDAO products = new ProductDAO();
    public InventoryServiceImpl() throws RemoteException { super(); }

    @Override public NetworkProduct getProduct(long productId) throws RemoteException {
        try { Product p = products.findById(productId); return p == null ? null : map(p); }
        catch (Exception exception) { throw new RemoteException("Product lookup failed.", exception); }
    }
    @Override public int getStock(long productId) throws RemoteException {
        try { return products.getStock(productId); }
        catch (Exception exception) { throw new RemoteException("Stock lookup failed.", exception); }
    }
    @Override public boolean updateStock(long productId, int stock) throws RemoteException {
        try { return products.setStock(productId, stock); }
        catch (Exception exception) { throw new RemoteException("Stock update failed.", exception); }
    }
    @Override public List<NetworkProduct> search(String term) throws RemoteException {
        try { return products.findAll(term, null, "name").stream().map(InventoryServiceImpl::map).toList(); }
        catch (Exception exception) { throw new RemoteException("Inventory search failed.", exception); }
    }
    private static NetworkProduct map(Product p) { return new NetworkProduct(p.getProductId(), p.getName(), p.getSalePrice(), p.getStock()); }
}
