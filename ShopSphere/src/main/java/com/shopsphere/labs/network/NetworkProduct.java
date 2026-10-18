package com.shopsphere.labs.network;

import java.io.Serializable;
import java.math.BigDecimal;

public record NetworkProduct(long id, String name, BigDecimal price, int stock) implements Serializable {
    private static final long serialVersionUID = 1L;
}
