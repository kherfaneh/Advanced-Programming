package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class NightMafia extends PlayingNight{
    //filed
    String name = whatIsMyName(this);

    //constructor
    NightMafia(Socket connectionSocket, ArrayList<ClientHandler> clients, ArrayList<String> clientsName) throws IOException {
        super(connectionSocket, clients, clientsName);
    }

    //method

    /**
     * This method performs the introduction night in the game. And introduce some players to each other
     */
    public void IntroductionNight(){
        String msgForGF = "Hi GodFather," + SMafiaName + " has the role of Simple Mafia in the game"
                + " and " + DLName + " has the role of Doctor Lector in the game";
        String msgForSM = "Hi Simple Mafia" + GFName + " has the role of God Father in the game"
                + " and " + DLName + " has the role of Doctor Lector in the game";
        String msgForDL = "Hi Simple Mafia" + GFName + " has the role of God Father in the game"
                + " and " + SMafiaName + " has the role of Simple Mafia in the game";


        sendOneOfClient(msgForGF, GFName);
        sendOneOfClient(msgForSM, SMafiaName);
        sendOneOfClient(msgForDL, DLName);

        String msgMayor = "Hi Mayor," + DoctorName + " has the role of Doctor in the game";
        String msgDoctor = "Hi Doctor" + MayorName + " has the role of Mayor in the game";

        sendOneOfClient(msgMayor,MayorName);
        sendOneOfClient(msgDoctor, DoctorName);
    }

    /**;
     * This method performs mafia-related operations at night
     * @throws IOException
     */
    public void PlayingMafia() throws IOException {
        ArrayList<String> ArraySlainName = new ArrayList<>();

        //1)Summoning and awakening the Mafia
        //(j) Send the list of all the players to the Mafia and
        // send the comments of the rest of the Mafia about the possible victim to the father-in-law
        sendOneOfClient("This is the list of citizens:" + RoleToName.toString(), GFName);
        sendOneOfClient("This is the list of citizens:" + RoleToName.toString(), SMafiaName);
        sendOneOfClient("This is the list of citizens:" + RoleToName.toString(),DLName);

        try {

            sendOneOfClient("Please enter the name of the person you want in 5 seconds:" + "\n" +
                    "Note: and you have not answered our questions twice so far, then remove from the game)", GFName);
            sendOneOfClient("Please enter the name of the person you want in 5 seconds:" + "\n" +
                    "Note: and you have not answered our questions twice so far, then remove from the game", SMafiaName);
            sendOneOfClient("Please enter the name of the person you want in 5 seconds:"  + "\n" +
                    "Note: and you have not answered our questions twice so far, then remove from the game", DLName);

            //(k) Each night is given time for players to play their part
            long start = System.currentTimeMillis( );
            String slainName = input.readLine();

            Thread.sleep(5000);

            long end = System.currentTimeMillis( );
            long dif = end - start;
            int Dif = (int) (dif * (0.001));

            if(Dif == 5){
                if(slainName != null){
                    ArraySlainName.add(slainName);
                    sendOneOfClient("Thank you for voting :)", name);
                }
                if(slainName == null){
                    CouponPlayerSet();
                    int index = CouponPlayer.get(name);
                    if(index == 2)
                        removeInPlay();
                }
                index++;
            }
            if(Dif > 5) {
                sendOneOfClient("Your time is up" + "Difference is : " + Dif + "\n" +
                        "You spent more time than the deadline to submit the name." , name);
                index++;
            }
        } catch (Exception e) {
            sendOneOfClient("Got an exception!", name);
        }

        //Investigate whether the person shot by the Mafia is rescued by a doctor
        if(saved.equals(slainNameRes))
            sendToAll("No one was killed last night :)");
        if(DieHardName.equals(slainNameRes)){
            if(DieHardLife == 0 || DieHardLife == 1) {
                if (DieHardLife == 0)
                    sendToAll("DieHard, a bullet was fired" + "\n" + "Then, no one was killed last night :)");
                if (DieHardLife == 1)
                    sendToAll("DieHard, was killed by two bullets");
                DieHardLife++;
            }
        }
        else {
            ////(o) a report of game information and events of last night was sent to everyone
            sendToAll(slainNameRes + " was killed last night :(");
            sendOneOfClient("You were killed by the Mafia. And you get out of the game. Goodbye :(", slainNameRes);

            //Remove the victim from the game
            removeInPlay();
        }
    }
}
