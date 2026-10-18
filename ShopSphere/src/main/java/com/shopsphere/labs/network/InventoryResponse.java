package com.shopsphere.labs.network;

import java.io.Serializable;
import java.util.List;

public record InventoryResponse(String message, List<NetworkProduct> products) implements Serializable {
    private static final long serialVersionUID = 1L;
}
