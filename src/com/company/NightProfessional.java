package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class NightProfessional extends PlayingNight{

    NightProfessional(Socket connectionSocket, ArrayList<ClientHandler> clients, ArrayList<String> clientsName) throws IOException {
        super(connectionSocket, clients, clientsName);
    }

    /**
     *This method performs actions related to the professional role at night
     * @throws IOException
     */
    public void PlayingProfessional() throws IOException {
        sendOneOfClient("Hello professional, do you want to use your role?" + "\n" +
                "Yes or No?",ProfessionalName);
        String answer = input.readLine();
        if(answer.equals("No")){
            sendOneOfClient("Ok, Thank You :)", ProfessionalName);
            sendToAll("professional is reluctant to use her(his) role in this round of play");
        }
        if(answer.equals("Yes")) {
            sendOneOfClient("You can shoot one of the players," + "\n" +
                    "If that player; Mafia will be removed from the game" + " If that player; Be a citizen;" +
                    " You will be removed from the game" + "\n" + "The list of players in the game is as " +
                    "follows" + "\n" + clientsName + "\n" + "Please enter a name:", ProfessionalName);
            String Target = input.readLine();
            TargetRes = RoleToName.get(Target);
            //Check that; The person who was shot; Is it Mafia or not
            ArrayList<String> Array = new ArrayList();
            Array.add(GFName);
            Array.add(SMafiaName);
            Array.add(DLName);
            for(int i = 0; i < Array.size(); i++){
                if(TargetRes.equals(Array.get(i))){
                    sendToAll("A mafia was killed by a professional");
                    clientsName.remove(Array.get(i));
                    RoleToName.remove(Array.get(i));
                    clientsNameDeleted.put(Array.get(i), RoleToName.get(Array.get(i)));
                    clients.remove(i);
                }
            }
        }
    }
}
