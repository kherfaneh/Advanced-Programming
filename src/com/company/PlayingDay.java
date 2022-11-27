package com.company;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayingDay extends SharedData implements Runnable{

    PlayingDay(Socket connectionSocket, ArrayList<ClientHandler> clients, ArrayList<String> clientsName) throws IOException {
        super(connectionSocket, clients, clientsName);
    }


    @Override
    public void run() {
        sendToAll("The night is over and the day is over, please wake up" + "\n" + "wake up? Yes or No:");
        try {
            String name = whatIsMyName(this);
            String msg = input.readLine();
            if(msg.contains("Yes")){
                sendOneOfClient("The chat phase begins.", name);
                Conversation();
                sendOneOfClient("According to the conversations that took place in the chat," +
                        " we enter the voting stage.", name);
                Voting(1000 * 30, "votingDay");
            }
            else if(msg.contains("No")){
                removeInPlay();
            }
            else if(msg.contains("HISTORY CHAT"))
                sendOneOfClient("You can access the text file that contains the chat history of" +
                        " this period of the game in the address (F:/MafiaPlay) in your system.", name);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }



    //method

    /**
     * It is a method that provides the conditions for conversation and chat between the players in the game
     * Also, the control players in the game can have a dialogue between the players
     */
    public void Conversation() {
        String inputChat = null;
        sendToAll("Dear player, you have 5 minutes to chat with the other players in the game" +
                " and share your opinions." + "\n" + "Let's go :)");
        try {
            long start = System.currentTimeMillis();
            do {
                String announcer = whatIsMyName(this);
                if(WitnessClient.contains(announcer)){
                    sendOneOfClient("You are not allowed to chat and you can not enter anything and" +
                            " only the ability to view other players' chats is enabled for you", announcer);
                    continue;
                }
                else{
                    inputChat = input.readLine();
                }
                //1000 * 60 * 5 = 300000 ms is 5 min
                Thread.sleep(1000 * 60 * 5);

                sendToAll(announcer + ":" + inputChat);
                Chat chat = new Chat(announcer, inputChat);
                writeObject("F:/MafiaPlay/far" + mode + ".txt", chat);

                long end = System.currentTimeMillis( );
                long dif = end - start;
                int Dif = (int) (dif * (0.001));
                if(Dif == 30000){
                    sendToAll("Conversation time is up, so please do not chat and" +
                            " enter anything from now on");
                    break;
                }
            } while (true);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * writeObject is a method for reading the contents of a text file that receives its name in the input
     * @param name is the name of the text file in question
     * @param note is the content of the desired text file
     * @throws FileNotFoundException
     */
    public void writeObject(String name, Chat note) throws FileNotFoundException {
        try(FileOutputStream file = new FileOutputStream(name)){
            ObjectOutputStream objectFile = new ObjectOutputStream(file);
            objectFile.writeObject(note);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readObject(String name) throws IOException {
        try(FileInputStream file = new FileInputStream(name)){
            ObjectInputStream objectFile = new ObjectInputStream(file);
            Chat note = (Chat) objectFile.readObject();
            System.out.println(note.getTeller() + ":" + note.getContent());
            System.out.println("\n");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}