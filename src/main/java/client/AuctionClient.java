package client;

import com.google.gson.Gson;
import server.Request;
import server.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class AuctionClient {
    private static final String HOST = "localhost";
    private static final int PORT = 8888;

    private final Gson gson;

    public AuctionClient() {
        this.gson = new Gson();
    }

    public Response sendRequest(Request request) {
        try (
                Socket socket = new Socket(HOST, PORT);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                )
        ) {
            String requestJson = gson.toJson(request);
            writer.println(requestJson);

            String responseJson = reader.readLine();

            return gson.fromJson(responseJson, Response.class);

        } catch (Exception e) {
            return Response.fail("Khong the ket noi server: " + e.getMessage());
        }
    }

    public Response getAuctions() {
        Request request = new Request("GET_AUCTIONS", new HashMap<>());
        return sendRequest(request);
    }

    public Response getAuctionById(int auctionId) {
        Map<String, String> data = new HashMap<>();
        data.put("auctionId", String.valueOf(auctionId));

        Request request = new Request("GET_AUCTION_BY_ID", data);
        return sendRequest(request);
    }

    public Response startAuction(int auctionId) {
        Map<String, String> data = new HashMap<>();
        data.put("auctionId", String.valueOf(auctionId));

        Request request = new Request("START_AUCTION", data);
        return sendRequest(request);
    }

    public Response cancelAuction(int auctionId) {
        Map<String, String> data = new HashMap<>();
        data.put("auctionId", String.valueOf(auctionId));

        Request request = new Request("CANCEL_AUCTION", data);
        return sendRequest(request);
    }

    public Response placeBid(int auctionId, int bidderId, BigDecimal amount) {
        Map<String, String> data = new HashMap<>();
        data.put("auctionId", String.valueOf(auctionId));
        data.put("bidderId", String.valueOf(bidderId));
        data.put("amount", amount.toString());

        Request request = new Request("PLACE_BID", data);
        return sendRequest(request);
    }
    public Response login(String username, String password) {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        Request request = new Request("LOGIN", data);
        return sendRequest(request);
    }

    public Response register(String username, String password, String role) {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("role", role);

        Request request = new Request("REGISTER", data);
        return sendRequest(request);
    }
}