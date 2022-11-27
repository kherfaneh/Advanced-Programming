package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerClient extends ConnectionPlayer {
    //filed
    /**
     * sign variable is used to check that the server is not running again
     */
    protected static boolean sign = true;

    //constructor
    public PlayerClient(Socket client) throws IOException {
        super(client);
    }

    public static void main(String[] args) throws IOException {

            //(b) Receive port number from user
            System.out.println("Hello, welcome to the Mafia game :)");
            System.out.println("Note:" + "\n" +
                    "1.Dear player, in any part of the game by " +
                    "entering the word EXIT you can leave the game" + "\n" +
                    "2.By entering the phrase WHAT IS MY NAME in any part of the game," +
                    " you can get your registered name in the game." + "\n" +
                    "3.By entering the word NEW NAME in any part of the game you can " +
                    "change your registered name in the game b" + "\n" +
                    "4.After chatting in the game and finishing it, you can access the chat" +
                    " history by entering HISTORY CHAT");
            System.out.println("Please complete and enter the following sentence according to the information below:");
            System.out.println("Mafia game port is number 8000.");
            System.out.println("Cyber Hunter game port is number 1025.");
            System.out.println("HorrorField game port is 1026.");
            System.out.println("\n");
            System.out.println("port number ...");
            Scanner scanner = new Scanner(System.in);
            String portNumber;
            while (true) {
                System.out.println("Please enter your desired port number:");
                String portNum = scanner.nextLine();
                if (portNum.equals("1027")) {
                    System.out.println("ok, thank you :)");
                    portNumber = "1027";
                    break;
                } else {
                    System.out.println("The game you are looking for is not available :(");
                    System.out.println("Please try again:");
                }
            }

            try (Socket client = new Socket("127.00.0.1", Integer.parseInt(portNumber))) {

                System.out.println("client connected to server!\n");
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                ConnectionPlayer connection = new ConnectionPlayer(client);

                new Thread(connection).start();

                while (true) {
                    String userText = scanner.nextLine();
                    if (userText.equals("EXIT")) {
                        break;
                    }
                    else
                        out.println(userText);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            scanner.close();
    }
}
