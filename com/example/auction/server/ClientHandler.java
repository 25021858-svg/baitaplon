package com.example.auction.server;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final RequestHandler requestHandler;
    private final Gson gson;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.requestHandler = new RequestHandler();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                                new JsonPrimitive(src.toString()))
                .create();
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String requestJson;

            while ((requestJson = reader.readLine()) != null) {
                Request request = gson.fromJson(requestJson, Request.class);

                Response response = requestHandler.handle(request);

                String responseJson = gson.toJson(response);

                writer.println(responseJson);
            }

        } catch (IOException e) {
            System.out.println("Client da ngat ket noi: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Khong the dong socket: " + e.getMessage());
        }
    }
}