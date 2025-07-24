package main.java.com.jaehyeoklim.tcp.chat.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static main.java.com.jaehyeoklim.tcp.chat.command.CommandPreprocessor.*;
import static main.java.com.jaehyeoklim.tcp.chat.util.Logger.log;

public class WriteHandler implements Runnable {

    private final DataOutputStream outputStream;
    private final Client client;

    private boolean isHandlerClosed = false;

    public WriteHandler(DataOutputStream outputStream, Client client) {
        this.outputStream = outputStream;
        this.client = client;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                String raw = scanner.nextLine();
                String message = process(raw);

                if (message == null || message.isEmpty()) continue;

                outputStream.writeUTF(message);
            }
        } catch (NoSuchElementException e) {
            log("No such message: " + e.getMessage());
        } catch (IOException e) {
            log("IOException: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    public void close() {
        if (isHandlerClosed) {
            return;
        }

        try {
            System.in.close();
        } catch (IOException e) {
            log("IOException: " + e.getMessage());
        }

        isHandlerClosed = true;
        log("WriteHandler closed");
    }
}
