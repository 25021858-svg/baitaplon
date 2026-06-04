package com.example.auction.dao;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:database/auction.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Chưa nhận file JAR SQLite trong Dependencies kìa ông ơi!");
            e.printStackTrace();
        }
    }

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        // 1. Tự động tạo thư mục 'database' ở gốc nếu anh chưa tạo bằng tay
        File dbFolder = new File("database");
        if (!dbFolder.exists()) {
            dbFolder.mkdirs();
        }

        // Tự động kết nối (Thằng SQLite thấy chưa có file auction.db là nó tự tạo luôn file mới)
        Connection connection = DriverManager.getConnection(URL);

        // 2. Kiểm tra xem bảng 'users' đã tồn tại trong file .db chưa
        boolean hasTables = false;
        try (ResultSet rs = connection.getMetaData().getTables(null, null, "users", null)) {
            if (rs.next()) {
                hasTables = true; // Bảng đã có rồi, không cần nạp lại nữa
            }
        }

        // 3. Nếu database trống trơn, tự động đọc 2 file SQL của nhóm trưởng để nạp dữ liệu
        if (!hasTables) {
            System.out.println("\n[DATABASE] >>>> Phát hiện database trống! Đang tự động nạp cấu trúc và dữ liệu mẫu...");
            try (Statement stmt = connection.createStatement()) {

                // Đọc và chạy file tạo bảng schema.sql
                if (new File("schema.sql").exists()) {
                    String schemaSql = new String(Files.readAllBytes(Paths.get("schema.sql")));
                    // Cắt từng câu lệnh SQL qua dấu chấm phẩy để chạy lần lượt
                    for (String sql : schemaSql.split(";")) {
                        if (!sql.trim().isEmpty()) {
                            stmt.execute(sql);
                        }
                    }
                    System.out.println("[DATABASE] >>>> Đã tạo xong các bảng: users, items, auctions, bids!");
                } else {
                    System.err.println("[DATABASE] >>>> KHÔNG tìm thấy file schema.sql ở thư mục gốc!");
                }

                // Đọc và chạy file bơm tài khoản seed.sql
                if (new File("seed.sql").exists()) {
                    String seedSql = new String(Files.readAllBytes(Paths.get("seed.sql")));
                    for (String sql : seedSql.split(";")) {
                        if (!sql.trim().isEmpty()) {
                            stmt.execute(sql);
                        }
                    }
                    System.out.println("[DATABASE] >>>> Đã nạp thành công tài khoản: seller1, bidder1, admin1!");
                } else {
                    System.err.println("[DATABASE] >>>> KHÔNG tìm thấy file seed.sql ở thư mục gốc!");
                }

                System.out.println("[DATABASE] >>>> KHỞI TẠO HOÀN TẤT! Chúc anh test app vui vẻ.\n");
            } catch (Exception e) {
                System.err.println("[DATABASE] Lỗi tự động nạp dữ liệu: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return connection;
    }
}