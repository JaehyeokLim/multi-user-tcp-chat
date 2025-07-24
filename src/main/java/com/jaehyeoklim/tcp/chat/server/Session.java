package main.java.com.jaehyeoklim.tcp.chat.server;

import main.java.com.jaehyeoklim.tcp.chat.command.CommandDispatcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.log;

public class Session implements Runnable {

    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final SessionManager sessionManager;
    private final CommandDispatcher commandDispatcher;

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

    @Override
    public void run() {
        try {
            while (true) {
                String receivedMessage = inputStream.readUTF();
                if (!isAuthenticated && !receivedMessage.startsWith("/login") && !receivedMessage.startsWith("/register")) {
                    send("로그인 후 이용 가능합니다. /login 또는 /register 명령을 사용해주세요.");
                    continue;
                }

                log("Received message from " + socket.getInetAddress().getHostName() + ": " + receivedMessage);
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
        log("Sending message to " + socket.getInetAddress().getHostName());
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            log("Error writing to socket: " + e.getMessage());
        }
    }

    public void close() {
        if (isSocketClosed) {
            return;
        }

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

        isSocketClosed = true;
        log("Closing session");
    }
}
