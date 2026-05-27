DROP TABLE IF EXISTS bids;
DROP TABLE IF EXISTS auctions;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE items(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    seller_id INTEGER NOT NULL,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    starting_price DECIMAL(15,2) NOT NULL,
    item_type VARCHAR(30) NOT NULL,
    created_at DATE NOT NULL,
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

CREATE TABLE auctions(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    item_id INTEGER NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    current_price DECIMAL(15,2) NOT NULL,
    current_winner_id INTEGER,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES items(id),
    FOREIGN KEY (current_winner_id) REFERENCES users(id)
);

CREATE TABLE bids (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    auction_id INTEGER NOT NULL,
    bidder_id INTEGER NOT NULL,
    bid_amount DECIMAL(15,2) NOT NULL,
    bid_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (auction_id) REFERENCES auctions(id),
    FOREIGN KEY (bidder_id) REFERENCES users(id)
);