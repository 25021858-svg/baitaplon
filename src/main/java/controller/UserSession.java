package controller;

public class UserSession {
    private static int currentUserId;
    private static String currentUsername;
    private static String currentRole;
    private static int selectedAuctionId;

    private UserSession() {
    }

    public static void login(int userId, String username, String role) {
        currentUserId = userId;
        currentUsername = username;
        currentRole = role;
    }

    public static void logout() {
        currentUserId = 0;
        currentUsername = null;
        currentRole = null;
        selectedAuctionId = 0;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

    public static int getSelectedAuctionId() {
        return selectedAuctionId;
    }

    public static void setSelectedAuctionId(int selectedAuctionId) {
        UserSession.selectedAuctionId = selectedAuctionId;
    }

    public static boolean isLoggedIn() {
        return currentUserId > 0;
    }

    public static boolean isBidder() {
        return "BIDDER".equals(currentRole);
    }

    public static boolean isSeller() {
        return "SELLER".equals(currentRole);
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(currentRole);
    }
}