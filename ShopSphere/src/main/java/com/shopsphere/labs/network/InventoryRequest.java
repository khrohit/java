package com.shopsphere.labs.network;

import java.io.Serializable;

public record InventoryRequest(String command, long productId, String searchTerm) implements Serializable {
    private static final long serialVersionUID = 1L;
}
