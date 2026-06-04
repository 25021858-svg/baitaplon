package com.example.auction;

// Import class User chuẩn của nhóm trưởng vào
import com.example.auction.model.User;

public class UserSession {
    // 1. Lưu nguyên cả đối tượng User thật (chứa được cả Bidder, Seller, Admin)
    public static User currentUser;

    // 2. Giữ nguyên 2 biến String này để các màn hình khác ông lỡ gọi rồi không bị lỗi
    public static String loggedInUsername;
    public static String loggedInRole;

    // Hàm xóa session khi người dùng bấm Đăng xuất (Logout)
    public static void clear() {
        currentUser = null;
        loggedInUsername = null;
        loggedInRole = null;
    }
}