package main.java.com.jaehyeoklim.tcp.chat.command;

import main.java.com.jaehyeoklim.tcp.chat.domain.User;
import main.java.com.jaehyeoklim.tcp.chat.repository.FileRepository;
import main.java.com.jaehyeoklim.tcp.chat.server.Session;
import main.java.com.jaehyeoklim.tcp.chat.server.SessionManager;

import java.io.IOException;

import static main.java.com.jaehyeoklim.tcp.chat.util.PasswordEncoder.*;
import static main.java.com.jaehyeoklim.tcp.chat.util.UUIDGenerator.*;

public class CommandController {

    private final FileRepository repository;
    private final SessionManager sessionManager;

    public CommandController(FileRepository repository, SessionManager sessionManager) {
        this.repository = repository;
        this.sessionManager = sessionManager;
    }

    @Mapping("/message")
    public void sendMessage(String message, Session session) throws IOException {
        sessionManager.sendToAll("[" + session.getUser().getName() + "] " + message);
    }

    @Mapping("/exit")
    public void exit(String message,  Session session) throws IOException {
        sessionManager.sendToAll(session.getUser().getName() + " has left the chat.");
        session.close();
    }

    @Mapping("/register")
    public void register(String message, Session session) throws IOException {
        String[] parts = message.split(" ", 3);

        if  (parts.length != 3) {
            session.send("Invalid command. Usage: /register <name> <id> <password>");
            return;
        }

        String name = parts[0];
        String loginId = parts[1];
        String hashedPassword = hash(parts[2]);

        if (repository.findByLoginId(loginId) != null) {
            session.send("This ID is already in use. Please choose another one.");
            return;
        }

        User newUser = new User(generateUUID(), loginId, hashedPassword, name);
        repository.add(newUser);
        session.send("Registration successful! You can now log in.");
    }

    @Mapping("/login")
    public void login(String body, Session session) throws IOException {
        String[] parts = body.split(" ", 2);

        if (parts.length != 2) {
            session.send("Invalid command. Usage: /login <id> <password>");
            return;
        }

        String loginId = parts[0];
        String password = parts[1];

        User user = repository.findByLoginId(loginId);

        if (user == null) {
            session.send("Login failed. Invalid username or password");
            return;
        }

        if (!matches(password, user.getPassword())) {
            session.send("Login failed. Invalid username or password");
            return;
        }

        session.authenticate(user);
        session.send("Login successful! Welcome, " + user.getName() + "!");
    }
}