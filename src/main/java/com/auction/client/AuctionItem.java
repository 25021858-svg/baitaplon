package com.auction.client;

public class AuctionItem {
    private String name;
    private double price;

    public AuctionItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
}