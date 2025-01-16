package org.example;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, String> users = new HashMap<>(); // UUID -> имя пользователя
    private String currentUser = null;

    public String registerUser(String username) {
        String uuid = java.util.UUID.randomUUID().toString();
        users.put(uuid, username);
        return uuid;
    }

    public boolean loginUser(String uuid) {
        if (users.containsKey(uuid)) {
            currentUser = uuid;
            return true;
        }
        return false;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}
