INSERT INTO users (username, password, role)
VALUES
('seller1', '123456', 'SELLER'),
('bidder1', '123456', 'BIDDER'),
('admin1', '123456', 'ADMIN');

INSERT INTO items (seller_id, name, description, starting_price, item_type, created_at)
VALUES
(1, 'ip13', 'hang moi', 500.22, 'ELECTRONICS', '2026-12-13T00:00:00');

INSERT INTO auctions (
    item_id,
    start_time,
    end_time,
    current_price,
    current_winner_id,
    status,
    created_at
)
VALUES
(1, '2026-12-13T10:00:00', '2026-12-13T12:00:00', 500.22, NULL, 'RUNNING', '2026-12-13T00:00:00');