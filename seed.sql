-- ========================================================================
-- 1. NẠP TÀI KHOẢN TEST (Thêm seller2 và bidder2 đua xe cho vui)
-- ========================================================================
INSERT INTO users (username, password, role, balance)
VALUES
    ('seller1', '123456', 'SELLER', 0.00),
    ('bidder1', '123456', 'BIDDER', 50000000.00),
    ('admin1', '123456', 'ADMIN', 0.00),
    ('seller2', '123456', 'SELLER', 0.00),
    ('bidder2', '123456', 'BIDDER', 75000000.00);

-- ========================================================================
-- 2. NẠP DANH SÁCH SẢN PHẨM (Đầy đủ 3 nhóm để test Ép kiểu Đa hình)
-- Đã bổ sung cột item_type để khớp với Class con trong Java
-- ========================================================================
INSERT INTO items (seller_id, name, description, starting_price, item_type, created_at)
VALUES
    (1, 'iPhone 13 Pro Max', 'Hàng người dùng lướt, pin 86%, xước dăm nhẹ viền', 500.22, 'ELECTRONICS', '2026-05-10 09:00:00'),
    (1, 'Tranh sơn mài Hồ Gươm', 'Tranh độc bản sáng tác năm 2022, có chữ ký tác giả', 1200.00, 'ART', '2026-05-15 14:30:00'),
    (4, 'Honda Wave Alpha 2004', 'Xe máy máy chất, biển số phố cổ, nguyên bản chưa bổ', 800.00, 'VEHICLE', '2026-05-18 11:00:00'),
    (4, 'MacBook Pro M1 2020', 'Bản 16GB RAM, 512GB SSD, ngoại hình đẹp không móp', 1500.50, 'ELECTRONICS', '2026-05-20 16:00:00'),
    (1, 'Bình gốm cổ Chu Đậu', 'Hàng trục vớt tàu đắm, bị nứt nhẹ một chút ở vành miệng', 3500.00, 'ART', '2026-05-22 08:00:00'),
    (4, 'Xe đạp Phượng Hoàng', 'Xe cổ thời bao cấp, xích líp còn nguyên tiếng kêu tanh tách', 300.00, 'VEHICLE', '2026-05-25 10:15:00');

-- ========================================================================
-- 3. NẠP DANH SÁCH PHIÊN ĐẤU GIÁ (Đầy đủ các Status để check tầng Service)
-- ========================================================================
INSERT INTO auctions (item_id, start_time, end_time, current_price, current_winner_id, status, created_at)
VALUES
-- Phiên 1: Đang chạy kịch tính, bidder1 đang dẫn đầu
(1, '2026-06-01 10:00:00', '2026-06-10 12:00:00', 1000.00, 2, 'RUNNING', '2026-06-01 10:00:00'),

-- Phiên 2: Đang chạy nhưng chưa có ma nào thèm vào đặt giá
(2, '2026-06-02 08:00:00', '2026-06-09 20:00:00', 1200.00, NULL, 'RUNNING', '2026-06-02 08:00:00'),

-- Phiên 3: Phiên sắp diễn ra (Lên lịch trước trong tương lai)
(3, '2026-06-15 09:00:00', '2026-06-22 17:00:00', 800.00, NULL, 'OPEN', '2026-06-03 11:00:00'),

-- Phiên 4: Đã kết thúc thành công, bidder2 (id=5) đã ăn được hàng với giá 1800
(4, '2026-05-20 07:00:00', '2026-06-02 23:00:00', 1800.00, 5, 'FINISHED', '2026-05-20 07:00:00'),

-- Phiên 5: Phiên đấu giá bị admin hủy bỏ do sản phẩm lỗi thiết kế
(5, '2026-05-25 13:00:00', '2026-05-28 13:00:00', 3500.00, NULL, 'CANCELED', '2026-05-25 13:00:00');

-- ========================================================================
-- 4. NẠP LỊCH SỬ ĐẶT GIÁ (BIDS) - Đẩy giá đè nhau liên tục
-- ========================================================================
INSERT INTO bids (auction_id, bidder_id, bid_amount, bid_time)
VALUES
-- Lịch sử nhảy giá của chiếc iPhone 13 (Auction 1)
(1, 2, 700.00, '2026-06-02 11:15:00'),
(1, 5, 900.00, '2026-06-03 14:20:00'),
(1, 2, 1000.00, '2026-06-04 09:05:00'), -- Giá cao nhất hiện tại thuộc về bidder1 (id=2)

-- Lịch sử nhảy giá của chiếc MacBook (Auction 4)
(4, 5, 1600.00, '2026-05-21 10:00:00'),
(4, 2, 1700.00, '2026-05-22 15:30:00'),
(4, 5, 1800.00, '2026-05-24 19:45:00'); -- Chốt phiên bidder2 ăn hàng