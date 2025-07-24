package main.java.com.jaehyeoklim.tcp.chat.command;

import main.java.com.jaehyeoklim.tcp.chat.server.Session;
import main.java.com.jaehyeoklim.tcp.chat.server.SessionManager;

import java.io.IOException;

public class CommandController {

    private final SessionManager sessionManager;

    public CommandController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Mapping("/message")
    public void sendMessage(String message, Session session) throws IOException {
        sessionManager.sendToAll(message);
    }

    @Mapping("/exit")
    public void exit(String message,  Session session) throws IOException {
        session.close();
    }
}