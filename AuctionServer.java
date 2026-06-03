package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AuctionServer {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Auction server dang chay o port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                System.out.println("Client moi ket noi: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException e) {
            System.out.println("Loi server: " + e.getMessage());
        }
    }
}