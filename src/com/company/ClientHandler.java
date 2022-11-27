package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends SharedData implements Runnable{

    ClientHandler(Socket connectionSocket, ArrayList<ClientHandler> clients, ArrayList<String> clientsName) throws IOException {
        super(connectionSocket, clients, clientsName);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = input.readLine();
                String clientName = whatIsMyName(this);
                CouponPlayerSet();
                if (msg.contains("port")) {
                    sendToAll("Please complete the following sentence about your personal information " +
                            "and enter it");
                    sendToAll("My name is ...");
                }
                if (msg.contains("name")) {
                    String PlayerName = msg.split(" ")[3];
                    int i = 0;
                    while (true) {
                        //(c) Check the uniqueness of the players' names.If the player name is not unique;
                        // The player name is not saved and a new name is requested
                        if (clientsName.contains(PlayerName)) {
                            sendOneOfClient("Your name has already been entered and registered by another player"
                                    + "\n" + "Please enter another name:" + "\n" + "My name is ...", clientName);
                            i++;
                        } else {
                            clientsName.add(PlayerName);
                            RoleDetermination(PlayerName);
                            break;
                        }
                    }
                    if (i != 0)
                        continue;

                    sendOneOfClient("Player Dear, please enter the word START to start the game to announce" +
                            " your readiness", clientName);
                } else {
                    //(d) Before the start of the game, players enter the word START to announce their readiness
                    if (msg.contains("START")) {
//                        String clientName = whatIsMyName(this);
                        sendOneOfClient("You are ready to play", clientName);
                        sendOneOfClient("1", clientName);
                        sendOneOfClient("2", clientName);
                        sendOneOfClient("3" + " Let's go ;)", clientName);
                        //(h) Send the right message and announce the day and night changes in the game
                        sendOneOfClient("Narrator: It is night. Round" + mode + "of the game begins", clientName);
                        if (mode == 0) {
                            try {
                                NightMafia night = new NightMafia(client, clients, clientsName);
                                night.IntroductionNight();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mode++;
                        } else {
                            NightMafia nightMafia = new NightMafia(client, clients, clientsName);
                            NightDoctor nightDoctor = new NightDoctor(client, clients, clientsName);
                            NightDetective nightDetective = new NightDetective(client, clients, clientsName);
                            NightProfessional nightProfessional = new NightProfessional(client, clients, clientsName);
                            NightPsychologist nightPsychologist = new NightPsychologist(client, clients, clientsName);
                            NightDieHard nightDieHard = new NightDieHard(client, clients, clientsName);

                            //1)Summoning and awakening the Mafia
                            nightMafia.PlayingMafia();
                            //2)Summoning and awakening the DoctorLector
                            nightDoctor.PlayingDoctor("DoctorLector");
                            //3)Summoning and awakening the Doctor
                            nightDoctor.PlayingDoctor("Doctor");
                            //4)Summoning and awakening the Detective
                            nightDetective.DetectivePlaying();
                            //5)Summoning and awakening the Professional
                            nightProfessional.PlayingProfessional();
                            //6)Summoning and awakening the Psychologist
                            nightPsychologist.PlayingPsychologist();
                            //7)Summoning and awakening the Psychologist
                            nightDieHard.PlayingDieHard();
                        }
                    } else {
                        //Scoring section: checking whether the user has not received a response as many as 3 times or not
                        CheckPlayerCoupons(clientName);
                    }
                }
                if (msg.contains("EXIT")) {
                    //(n) At any time from the game, the user can exit the game by entering the word EXIT
                    //(f) If a player is eliminated from the game, there are two choices for her(him)
                    CheckPlayerCoupons(clientName);
                }
                else if (msg.contains("NEW NAME")) {
                    String newName = msg.split(" ")[1];
                    changeNameOfClient(this, newName);

                } else if (msg.contains("WHAT IS MY NAME")) {
                    whatIsMyName(this);

                } else if (msg.contains("victim")) {
                    ////(h) Send the right message and announce the day and night changes in the game
                    sendToAll("Narrator: We examine the status of the game in terms of announcing" +
                            " the winner and loser" + "\n" + "Then we announce the winner and loser in the game :)");
                    EndOfGame();
                    break;
                }

                else {
                    //do some thing
                    sendToAll("Please enter the input according to the requested items. Thank you ;)");
                }

            }
        } catch (IOException e) {
            System.err.println(e);
        } catch (NullPointerException e) {
            //it means this user should be remove
            //because he was disconnected from server and get null msg
            try {
                removeInPlay();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
