package main.java.com.jaehyeoklim.tcp.chat.server.util;

import java.util.UUID;

public abstract class UUIDGenerator {

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }
}
