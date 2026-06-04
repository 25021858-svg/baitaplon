package com.example.auction;

import java.util.HashMap;

public class UserSession {
    public static String loggedInRole = "Buyer";
    public static String loggedInUsername = "Guest";
    public static UserInfo currentUser;

    public static HashMap<String, UserInfo> userDatabase = new HashMap<>();

    static {
        userDatabase.put("admin", new UserInfo("admin", "123", "Admin"));
        userDatabase.put("seller", new UserInfo("seller", "123456", "Seller"));
        userDatabase.put("buyer", new UserInfo("buyer", "123456", "Buyer"));
    }

    private static UserSession instance;
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public HashMap<String, UserInfo> getUserDatabase() {
        return userDatabase;
    }

    public static class UserInfo {
        public String username;
        public String password;
        public String role;

        public UserInfo(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
}