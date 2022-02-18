package com.company.dymrin26_1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientHandlerMessager {
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandlerMessager(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public String listenInboundMessage() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("SWW during inbound message listening.", e);
        }
    }

    public void sendOutboundMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
