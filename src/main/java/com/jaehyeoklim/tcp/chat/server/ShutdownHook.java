package main.java.com.jaehyeoklim.tcp.chat.server;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.log;

public class ShutdownHook implements Runnable {

    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final SessionManager sessionManager;

    public ShutdownHook(ServerSocket serverSocket, ExecutorService executorService , SessionManager sessionManager) {
        this.serverSocket = serverSocket;
        this.executorService = executorService;
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {
        log("starting shutdown hook");
        try {
            sessionManager.closeAll();
            serverSocket.close();
            executorService.shutdownNow();
        } catch (Exception e) {
            log("Error accepting connection: " +  e.getMessage());
        }
    }
}
