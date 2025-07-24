package main.java.com.jaehyeoklim.tcp.chat.client.main;

import main.java.com.jaehyeoklim.tcp.chat.client.network.Client;
import java.io.IOException;

public class ClientMain {

    private static final int PORT = 54321;

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", PORT);
        client.start();
        displayStartupInstructions();
    }

    private static void displayStartupInstructions() {
        System.out.println("========================================");
        System.out.println("📢 Welcome to the TCP Multi Chat Program!");
        System.out.println("----------------------------------------");
        System.out.println("This is a multi-user terminal-based chat system.");
        System.out.println("To participate in chatting, you must register or log in.");
        System.out.println();
        System.out.println("Available commands:");
        System.out.println("  ▶ /register <loginId> <password> <name>");
        System.out.println("     → Register as a new user");
        System.out.println("  ▶ /login <loginId> <password>");
        System.out.println("     → Log in with your credentials");
        System.out.println("  ▶ /exit");
        System.out.println("     → Exit the chat program");
        System.out.println("========================================");
    }
}
