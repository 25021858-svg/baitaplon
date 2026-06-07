package client;

import server.Response;

import java.math.BigDecimal;

public class TestAuctionClient {
    public static void main(String[] args) {
        AuctionClient client = new AuctionClient();

        Response loginResponse = client.login("bidder1", "123456");
        System.out.println(loginResponse.getMessage());
        System.out.println(loginResponse.getData());

        Response getAuctionsResponse = client.getAuctions();
        System.out.println(getAuctionsResponse.getMessage());
        System.out.println(getAuctionsResponse.getData());

        Response bidResponse = client.placeBid(1, 2, new BigDecimal("1500.00"));
        System.out.println(bidResponse.getMessage());

        Response itemsResponse = client.getItems();
        System.out.println(itemsResponse.getMessage());
        System.out.println(itemsResponse.getData());

        Response sellerItemsResponse = client.getItemsBySeller(1);
        System.out.println(sellerItemsResponse.getMessage());
        System.out.println(sellerItemsResponse.getData());

        Response bidsResponse = client.getBidsByAuction(1);
        System.out.println(bidsResponse.getMessage());
        System.out.println(bidsResponse.getData());

        Response deleteItemResponse = client.deleteItem(999);
        System.out.println(deleteItemResponse.getMessage());
    }
}