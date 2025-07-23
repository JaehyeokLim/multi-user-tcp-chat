package main.java.com.jaehyeoklim.tcp.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.log;

public class Client {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final String host;
    private final int port;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private ReadHandler readHandler;
    private WriteHandler writeHandler;

    private boolean isSocketClosed = false;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException {
        log("Client starting on port " + port);
        socket = new Socket(host, port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        readHandler = new ReadHandler(inputStream, this);
        writeHandler = new WriteHandler(outputStream, this);

        executorService.execute(readHandler);
        executorService.execute(writeHandler);
    }

    public synchronized void close() {
        if (isSocketClosed) {
            return;
        }

        writeHandler.close();
        readHandler.close();

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log("Error closing input stream: " + e.getMessage());
            }
        }

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                log("Error closing output stream: " + e.getMessage());
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                log("Error closing socket: " + e.getMessage());
            }
        }

        executorService.shutdownNow();
        isSocketClosed = true;
        log("Client closed");
    }
}
