package server;

import service.AuctionService;

import java.net.ServerSocket;
import java.net.Socket;

public class AuctionServer {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        startAutoCloseAuctionTask();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("AuctionServer dang chay o port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startAutoCloseAuctionTask() {
        Thread thread = new Thread(() -> {
            AuctionService auctionService = new AuctionService();

            while (true) {
                try {
                    auctionService.closeExpireAuctions();
                    Thread.sleep(10000);
                } catch (Exception e) {
                    System.out.println("Loi auto close auction: " + e.getMessage());
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
}