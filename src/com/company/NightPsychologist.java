package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class NightPsychologist extends PlayingNight{

    NightPsychologist(Socket connectionSocket, ArrayList<ClientHandler> clients, ArrayList<String> clientsName) throws IOException {
        super(connectionSocket, clients, clientsName);
    }

    /**
     * This method performs actions related to the role of "psychologist" at night
     * @throws IOException
     */
    public void PlayingPsychologist() throws IOException {

        sendOneOfClient("Hello Psychologist, do you want to use your role?" + "\n" +
                "Yes or No?", PsychologistName);
        String answer = input.readLine();
        if (answer.equals("No")) {
            sendOneOfClient("Ok, Thank You :)", ProfessionalName);
            sendToAll("Psychologist is reluctant to use her(his) role in this round of play");
        }
        if(answer.equals("Yes")){

            sendOneOfClient("You can choose one of the players to be silent for the day in the game and" +
                    " not have the right to comment."+ "The list of players in the game is as follows" +
                    "\n"  + clientsName + "\n" + "Please enter a name:", ProfessionalName);
        }
        psychologistGoal = input.readLine();
        sendOneOfClient("Thank you Professional", ProfessionalName);
        sendToAll("The psychologist exercised her(his) right to silence the sex of the players");
    }
}
