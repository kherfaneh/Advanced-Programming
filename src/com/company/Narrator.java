package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Narrator {

    //roleToName is a HashMap that is intended to match the names of players and their roles(namesOfClient)
//    private static HashMap<String, String> RoleToName = new HashMap<>();
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ArrayList<String> namesOfClients = new ArrayList<>();
//    private static HashMap<String, String> clientNameDeleted = new HashMap<>();
    private static ExecutorService executerPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try (ServerSocket welcomeSocket = new ServerSocket(1027)) {
            System.out.println("Narrator is Waiting...");
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("server accepted a client");
                ClientHandler client = new ClientHandler(connectionSocket, clients, namesOfClients);
                executerPool.execute(client);
                clients.add(client);
                PlayingDay playingDay = new PlayingDay(connectionSocket, clients, namesOfClients);
                executerPool.execute(playingDay);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

