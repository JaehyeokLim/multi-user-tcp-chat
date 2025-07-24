package main.java.com.jaehyeoklim.tcp.chat.server.session;

import main.java.com.jaehyeoklim.tcp.chat.server.command.CommandDispatcher;
import main.java.com.jaehyeoklim.tcp.chat.server.domain.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.*;
import static main.java.com.jaehyeoklim.tcp.chat.util.ResourceCloser.*;

public class Session implements Runnable {

    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final SessionManager sessionManager;
    private final CommandDispatcher commandDispatcher;

    private User user;

    private boolean isAuthenticated = false;
    private boolean isSocketClosed = false;

    public Session(Socket socket, SessionManager sessionManager, CommandDispatcher commandDispatcher) throws IOException {
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.sessionManager = sessionManager;
        this.commandDispatcher = commandDispatcher;

        this.sessionManager.addSession(this);
    }

    public void authenticate(User user) {
        this.user = user;
        this.isAuthenticated = true;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String receivedMessage = inputStream.readUTF();
                if (!isAuthenticated && !receivedMessage.startsWith("/login") && !receivedMessage.startsWith("/register")) {
                    send("Please use the /login or /register command.");
                    continue;
                }

                log("Received message from " + socket.getInetAddress().getHostName() + " " + socket.getPort() + ": " + receivedMessage);
                commandDispatcher.dispatch(receivedMessage, this);
            }
        } catch (IOException e) {
            log("Error reading from socket: " + e.getMessage());
        } finally {
            sessionManager.removeSession(this);
            close();
        }
    }

    public void send(String message) {
        log("Sending message to " + socket.getInetAddress().getHostName() + " " + socket.getPort() + ": " + message);
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            log("Error writing to socket: " + e.getMessage());
        }
    }

    public synchronized void close() {
        if (isSocketClosed) {
            return;
        }

        closeAll(socket, inputStream, outputStream);

        isSocketClosed = true;
        log("Closing session");
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public User getUser() {
        return user;
    }
}
