package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class NightDoctor extends PlayingNight{
    //filed
    private int CouponDoctor = 0;
//    protected String savedLector;
//    protected String saved;

    NightDoctor(Socket connectionSocket, ArrayList<ClientHandler> clients, ArrayList<String> clientsName) throws IOException {
        super(connectionSocket, clients, clientsName);
    }

    /**
     *Actions related to the role of the doctor or doctor Lecter at night; It is done in this method
     * @param DoctorTurn is a variable that specifies whether the role in question is a doctor or a doctor
     * @throws IOException
     */
    public void PlayingDoctor(String DoctorTurn) throws IOException {
        if(DoctorTurn.equals("DoctorLector"))
            DoctorTurn = DLName;
        if(DoctorTurn.equals("Doctor"))
            DoctorTurn = super.DoctorName;


        String msgForDoctor = "Hello" + DoctorTurn + ", you are allowed to save one of the players in each round of the game." +
                " Or you can save your life in one round of the game:" + "\n" + "Which do you choose?" +
                "\n" + "1.Your life?" + "\n" + "2.Another player's life?";
        sendOneOfClient(msgForDoctor, DoctorTurn);
        int choosingDoctor = input.read();
        if(choosingDoctor == 1){
            if(CouponDoctor == 0) {
                if(DoctorTurn.equals("DoctorLector"))
                    savedLector = DoctorTurn;
                else
                    saved = DoctorTurn;
                CouponDoctor++;
            }
            else
                sendOneOfClient( DoctorTurn + ", you once used the opportunity to save your life and now you are" +
                        " only able to save the life of one of the players", DoctorTurn);
        }
        sendOneOfClient("So please enter the player name according to the list of player names:", DoctorTurn);
        sendOneOfClient("List of player names:" + "\n" + clientsName, DoctorTurn);
        if(DoctorTurn.equals("DoctorLector")) {
            savedLector = input.readLine();
        }
        else {
            saved = input.readLine();

        }

//        if(saved.equals(slainNameRes))
//            sendToAll("No one was killed last night :)");
//        else {
//            ////(o) a report of game information and events of last night was sent to everyone
//            sendToAll(slainNameRes + " was killed last night :(");
//            sendOneOfClient("You were killed by the Mafia. And you get out of the game. Goodbye :(", slainNameRes);
//
//            //Remove the victim from the game
//            String name = whatIsMyName(this);
//            if(name.equals(slainNameRes))
//                clients.remove(this);
//
//            clientsName.remove(slainNameRes);
//            RoleToName.remove(slainNameRes);
//        }

        //(o) a report of game information and events of last night was sent to everyone
        sendToAll("The list of live players in the game so far is as follows:" + "\n" + clientsName);
    }
}
