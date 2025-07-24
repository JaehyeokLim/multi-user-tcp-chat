package main.java.com.jaehyeoklim.tcp.chat.client.handler;

import main.java.com.jaehyeoklim.tcp.chat.client.network.Client;

import java.io.DataInputStream;
import java.io.IOException;

import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.*;

public class ReadHandler implements Runnable {

    private final DataInputStream inputStream;
    private final Client client;

    private boolean isHandlerClosed = false;

    public ReadHandler(DataInputStream inputStream, Client client) {
        this.inputStream = inputStream;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String receivedMessage = inputStream.readUTF();
                System.out.println(receivedMessage);
            }
        } catch (IOException e) {
            log("IOException: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    public void close() {
        if (isHandlerClosed) {
            return;
        }

        isHandlerClosed = true;
        log("ReadHandler closed");
    }
}
