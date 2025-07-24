package main.java.com.jaehyeoklim.tcp.chat.server;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.log;

/**
 * JVM 종료 시 리소스를 안전하게 정리하기 위한 종료 훅(Shutdown Hook) 클래스.
 *
 * <p>서버 소켓, 스레드 풀, 세션 매니저를 종료하며
 * ExecutorService 를 shutdownNow()로 강제 종료함.</p>
 */
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
