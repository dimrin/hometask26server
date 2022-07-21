package com.company.dymrin26_1.engine;

import com.company.dymrin26_1.client_handler.ClientHandler;
import com.company.dymrin26_1.client_handler.ClientHandlerRegistry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerEngine {

    private ClientHandlerRegistry clientHandlerRegistry;

    public void start() {
        try {
            clientHandlerRegistry = new ClientHandlerRegistry();
            ServerSocket socket = new ServerSocket(8888);
            while (socket.isBound()) {
                Socket accept = socket.accept();
                new Thread(() -> new ClientHandler(this, accept)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW during server start-up.", e);
        }
    }

    public void register(String name, ClientHandler clientHandler) {
        clientHandlerRegistry.register(name, clientHandler);
    }


    public boolean isLogged(String name) {
        return clientHandlerRegistry.isLoggedIn(name);
    }

    public void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlerRegistry.getHandlers().values()) {
            clientHandler.getMessager().sendOutboundMessage(message);
        }
    }

    public void deleteUser(String name, ClientHandler clientHandler) {
        clientHandlerRegistry.deleteUser(name, clientHandler);
    }


}
