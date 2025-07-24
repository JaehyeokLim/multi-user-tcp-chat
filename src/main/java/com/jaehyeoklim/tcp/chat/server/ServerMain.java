package main.java.com.jaehyeoklim.tcp.chat.server;

import main.java.com.jaehyeoklim.tcp.chat.command.CommandController;
import main.java.com.jaehyeoklim.tcp.chat.command.CommandDispatcher;
import main.java.com.jaehyeoklim.tcp.chat.repository.FileRepository;

import java.io.IOException;
import java.util.List;

public class ServerMain {

    private static final int PORT = 54321;

    public static void main(String[] args) throws IOException {
        FileRepository repository = new FileRepository();
        SessionManager sessionManager = new SessionManager();
        CommandController commandController = new CommandController(repository, sessionManager);
        CommandDispatcher commandDispatcher = new CommandDispatcher(List.of(commandController));

        Server server = new Server(PORT, sessionManager, commandDispatcher);
        server.start();
    }
}
