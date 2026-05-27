INSERT INTO users (username, password, role)
VALUES
('seller1', '123456', 'SELLER'),
('bidder1', '123456', 'BIDDER'),
('admin1', '123456', 'ADMIN');

INSERT INTO items (seller_id, name, description, starting_price,item_type, created_at)
VALUES
(1, 'ip13', 'hang moi', 500.22,'hang de vo','2025-12-13');

INSERT INTO auctions (item_id, start_time, end_time, current_price, current_winner_id, status)
VALUES
(1, '2025-12-13 10:00:00', '2025-12-13 12:00:00', 500.22, NULL, 'RUNNING');

INSERT INTO bids (auction_id, bidder_id, bid_amount)
VALUES
(1, 2, 1000.00);