package main.java.com.jaehyeoklim.tcp.chat.server.main;

import main.java.com.jaehyeoklim.tcp.chat.server.session.SessionManager;
import main.java.com.jaehyeoklim.tcp.chat.server.command.CommandController;
import main.java.com.jaehyeoklim.tcp.chat.server.command.CommandDispatcher;
import main.java.com.jaehyeoklim.tcp.chat.server.repository.FileRepository;

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
