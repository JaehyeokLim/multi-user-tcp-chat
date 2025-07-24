package main.java.com.jaehyeoklim.tcp.chat.server.main;

import main.java.com.jaehyeoklim.tcp.chat.server.command.CommandDispatcher;

import main.java.com.jaehyeoklim.tcp.chat.server.session.Session;
import main.java.com.jaehyeoklim.tcp.chat.server.session.SessionManager;
import main.java.com.jaehyeoklim.tcp.chat.server.util.ShutdownHook;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.*;

public class Server {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final SessionManager sessionManager;
    private final CommandDispatcher commandDispatcher;

    private final int port;

    private ServerSocket serverSocket;

    public Server(int port, SessionManager sessionManager, CommandDispatcher commandDispatcher) {
        this.port = port;
        this.sessionManager = sessionManager;
        this.commandDispatcher = commandDispatcher;
    }

    public void start() throws IOException {
        log("Starting server on port " + port);
        serverSocket = new ServerSocket(port);
        log("Server listening on port " + port);

        addShutdownHook();
        run();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                new ShutdownHook(serverSocket, executorService, sessionManager)
        ));
    }

    private void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                Session session = new Session(socket, sessionManager, commandDispatcher);
                executorService.submit(session);
                log("Accepted connection from " + socket.getInetAddress().getHostName());

            }
        } catch (IOException e) {
            log("Error accepting connection: " +  e.getMessage());
        }
    }
}
