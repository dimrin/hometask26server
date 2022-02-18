package com.company.dymrin26_1;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ClientHandler {

    private ServerEngine serverEngine;
    private ClientHandlerMessager messager;
    private String name;

    public ClientHandler(ServerEngine serverEngine, Socket socket) {
        try {
            System.out.printf("[SERVER | %s] Client handler starting...%n", new Date());

            this.serverEngine = serverEngine;
            messager = new ClientHandlerMessager(
                    new DataInputStream(socket.getInputStream()),
                    new DataOutputStream(socket.getOutputStream())
            );

            doAuthentication();
            doListen();
            System.out.printf("[SERVER | %s] [%s] has disconnected from the server.%n", new Date(), name);
            serverEngine.broadcastMessage(String.format("[%s] has disconnected from the server.", name));
            serverEngine.deleteUser(name, this);
        } catch (IOException e) {
            throw new RuntimeException("SWW during the client connection.", e);
        }
    }

    public ClientHandlerMessager getMessager() {
        return messager;
    }

    private void doAuthentication() {
        System.out.printf("[SERVER | %s] Attempt of client authentication ...%n", new Date());
        messager.sendOutboundMessage("Please enter -login -u username to connect to the chat");
        String credentials = messager.listenInboundMessage();
        System.out.printf("[SERVER | %s] Authentication payload: %s%n", new Date(), credentials);
        while (!credentials.contains("-login -u")) {
            messager.sendOutboundMessage("You entered data data by wrong format, please enter again in correct format: -login -u username");
            System.out.printf("[SERVER | %s] Authentication failed: client entered data by wrong format: %s%n", new Date(), credentials);
            credentials = messager.listenInboundMessage();
        }
        if (credentials.contains("-login -u")) {
            String[] splitCredentials = credentials.split("\\s");
            String name = splitCredentials[2];

            if (serverEngine.isLogged(name)) {
                messager.sendOutboundMessage(String.format("This name [%s] already have selected, please choose another one.%n", name));
                while (serverEngine.isLogged(name)) {
                    name = messager.listenInboundMessage();
                }
            }

            serverEngine.broadcastMessage(String.format("[%s] connected to the server.", name));
            serverEngine.register(name, this);
            messager.sendOutboundMessage("You successfully connected to the server.");
            this.name = name;

            System.out.printf("[SERVER | %s] Client with name [%s] authenticated successfully.%n", new Date(), name);
            return;

        }

        System.out.printf("[SERVER | %s] Client authentication was not successful.%n", new Date());
    }

    private void doListen() {
        System.out.printf("[SERVER | %s] Client with name [%s] has started to listen inbound messages.%n", new Date(), name);

        while (true) {
            String inboundMessage = messager.listenInboundMessage();
            System.out.printf("[SERVER | %s] Client with name [%s] initiates message [%s] broadcast.%n", new Date(), name, messager);
            if (inboundMessage.equals("-logout")) {
                break;
            }
            serverEngine.broadcastMessage(String.format("%s: %s", name, inboundMessage));

        }
    }
}
