package com.shopsphere.labs.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class InventorySocketClient {
    public static void main(String[] args) throws Exception {
        String command = args.length > 0 ? args[0] : "SEARCH_PRODUCT";
        long productId = args.length > 1 ? Long.parseLong(args[1]) : 1;
        String search = args.length > 2 ? args[2] : "";
        try (Socket socket = new Socket("127.0.0.1", 5055);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.flush();
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                out.writeObject(new InventoryRequest(command, productId, search)); out.flush();
                InventoryResponse response = (InventoryResponse) in.readObject();
                System.out.println(response.message());
                response.products().forEach(System.out::println);
            }
        }
    }
}
