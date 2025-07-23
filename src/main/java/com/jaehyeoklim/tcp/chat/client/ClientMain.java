package main.java.com.jaehyeoklim.tcp.chat.client;

import java.io.IOException;

public class ClientMain {

    private static final int PORT = 54321;

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", PORT);
        client.start();
    }
}
