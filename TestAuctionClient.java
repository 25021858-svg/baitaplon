package client;

import server.Response;

import java.math.BigDecimal;

public class TestAuctionClient {
    public static void main(String[] args) {
        AuctionClient client = new AuctionClient();

        Response getAuctionsResponse = client.getAuctions();
        System.out.println(getAuctionsResponse.getMessage());
        System.out.println(getAuctionsResponse.getData());

        Response bidResponse = client.placeBid(1, 2, new BigDecimal("1000.00"));
        System.out.println(bidResponse.getMessage());

        Response loginResponse = client.login("bidder1", "123456");
        System.out.println(loginResponse.getMessage());
        System.out.println(loginResponse.getData());

        Response registerResponse = client.register("newbidder", "123456", "BIDDER");
        System.out.println(registerResponse.getMessage());
    }
}