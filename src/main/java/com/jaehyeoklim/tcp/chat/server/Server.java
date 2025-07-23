package main.java.com.jaehyeoklim.tcp.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.log;

public class Server {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final SessionManager sessionManager;
    private final int port;

    private ServerSocket serverSocket;

    public Server(int port, SessionManager sessionManager) {
        this.port = port;
        this.sessionManager = sessionManager;
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
                Session session = new Session(socket, sessionManager);
                executorService.submit(session);
                log("Accepted connection from " + socket.getInetAddress().getHostName());

            }
        } catch (IOException e) {
            log("Error accepting connection: " +  e.getMessage());
        }
    }
}
