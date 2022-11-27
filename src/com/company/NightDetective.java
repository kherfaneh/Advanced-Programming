package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class NightDetective extends PlayingNight{
    NightDetective(Socket connectionSocket, ArrayList<ClientHandler> clients, ArrayList<String> clientsName) throws IOException {
        super(connectionSocket, clients, clientsName);
    }

    /**
     *Actions related to the role of the detective at night; It is done in this method
     * @throws IOException
     */
    public void DetectivePlaying() throws IOException {
        sendOneOfClient("Hi Detective, you can say the name of one of the players and get an inquiry." +
                "\n" + "So, the answer to this query will be true or false" + "\n" + "The names of the players in" +
                " the game are as follows:", DetectiveName);

        sendOneOfClient(clientsName.toString(), DetectiveName);
        String InquiryDetective = input.readLine();
        if(RoleToName.get(InquiryDetective).equals("SimpleMafia") || RoleToName.get(InquiryDetective).equals("DoctorLector"))
            sendOneOfClient("true", DetectiveName);
        else
            sendOneOfClient("false", DetectiveName);
    }
}
