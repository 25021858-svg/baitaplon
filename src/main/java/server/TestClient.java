package server;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TestClient {
    private static final String HOST = "localhost";
    private static final int PORT = 8888;

    public static void main(String[] args) {
        testGetAuctions();
        testPlaceBid();
    }

    private static void testGetAuctions() {
        Request request = new Request("GET_AUCTIONS", new HashMap<>());

        sendRequest(request);
    }

    private static void testPlaceBid() {
        Map<String, String> data = new HashMap<>();
        data.put("auctionId", "1");
        data.put("bidderId", "2");
        data.put("amount", "800.00");

        Request request = new Request("PLACE_BID", data);

        sendRequest(request);
    }

    private static void sendRequest(Request request) {
        Gson gson = new Gson();

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

            System.out.println("Response:");
            System.out.println(responseJson);
            System.out.println("-------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}