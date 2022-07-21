package com.company.dymrin26_1.client_handler;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandlerRegistry {
    private final Map<String, ClientHandler> handlers = new ConcurrentHashMap<>();

    public Map<String, ClientHandler> getHandlers() {
        return handlers;
    }

    public void register(String name, ClientHandler clientHandler) {
        handlers.put(name, clientHandler);
    }

    public boolean isLoggedIn(String name) {
        for (String handlerName : handlers.keySet()) {
            if (handlerName.equals(name)) {
                return true;
            }
        }
        return false;
    }


    public void deleteUser(String name, ClientHandler clientHandler) {
        handlers.remove(name, clientHandler);
    }
}
