package main.java.com.jaehyeoklim.tcp.chat.command;

public abstract class CommandPreprocessor {

    public static String process(String message) {
        if (message.startsWith("/") && !message.startsWith("/message")) {
            return message;
        } else {
            return "/message " + message;
        }
    }
}
