package main.java.com.jaehyeoklim.tcp.chat.domain;

import java.util.UUID;

public class User {

    private final UUID id;
    private final String loginId;
    private String password;
    private String name;

    public User(UUID id, String loginId, String name, String password) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("Member[UUID=%s, loginId=%s, name=%s]", id, loginId, name);
    }
}
