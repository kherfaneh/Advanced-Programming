package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class SharedData {
    //filed
    //ArrayList clients in order to save clients
    protected ArrayList<ClientHandler> clients ;
    //ArrayList clientsName stores the names of the players in the game
    protected ArrayList<String> clientsName;
    //ArrayList clientsNameDeleted stores the names of players who have been removed from the game
    protected HashMap<String, String> clientsNameDeleted;
    //HashMap RoleToName  matches the name of each player with her(his) respective role in the game
    protected HashMap<String, String> RoleToName;
    //ArrayList WitnessClient stores the names of control players in the game
    protected ArrayList<String> WitnessClient;
    //ArrayList ArraySlainName stores the names of candidates to be killed by the Mafia
    protected ArrayList<String> ArraySlainName;
    //HashMap voting matches the names of the candidates with the number of votes cast for them
    protected HashMap<String, Integer>voting;
    //HashMap CouponPlayer compares the names of people and the number of unanswered questions in the game (score section)
    protected HashMap<String, Integer> CouponPlayer;
    protected int index = 0;
    protected static String msgSlainName;
    protected static String slainNameRes;
    protected final String SMafiaName = RoleToName.get("SimpleMafia");
    protected final String DLName = RoleToName.get("DoctorLector");
    protected final String GFName = RoleToName.get("GodFather");
    protected final String MayorName = RoleToName.get("Mayor");
    protected final String DoctorName = RoleToName.get("Doctor");
    protected final String DetectiveName = RoleToName.get("Detective");
    protected final String ProfessionalName = RoleToName.get("Professional");
    protected final String PsychologistName = RoleToName.get("Psychologist");
    protected final String DieHardName = RoleToName.get("DieHard");

    //mode for playing and voting
    //0 menas playing and 1 means voting
    protected static int mode = 0;

    protected BufferedReader input;
    protected Socket client;
    PrintWriter output;

    //constructor
    SharedData(Socket connectionSocket, ArrayList<ClientHandler> clients,
               ArrayList<String> clientsName) throws IOException {
        this.clients = clients;
        this.clientsName = clientsName;

        InputStreamReader reader = new InputStreamReader(connectionSocket.getInputStream());
        this.input = new BufferedReader(reader);

        this.output = new PrintWriter(connectionSocket.getOutputStream(), true);
    }


    /**
     * //This method changes the name of the player in the game
     * @param client is player
     * @param newName is new name
     */
    void changeNameOfClient(ClientHandler client, String newName){
        for(int i=0; i<clients.size(); i++){
            if(clients.get(i) == client){
                clientsName.set(i, newName );
                client.output.println("your name have been changed successfully\n Your new name is: "+ newName+"\n");
                break;
            }
        }
    }


    /**
     * By this method, the player gets her(his) name saved in the game
     * @param client is the player who asked for her(his) name
     * @return
     */
    String whatIsMyName(SharedData client){
        for(int i=0; i<clients.size(); i++){
            if(clients.get(i) == client){
                String clientName = clientsName.get(i);
                return clientName;
            }
        }
        return "";
    }


    /**
     * This method sends a specific message to all players in the game
     * @param msg is a message that is to be sent to all player
     */
    void sendToAll(String msg){
        for(ClientHandler client: clients){
            System.out.println(client);
            client.output.println(msg);
        }
    }


    /**
     * This method sends a specific message to a specific player in the game
     * @param msg is a message that is to be sent to a specific player
     * @param clientName is player in question
     */
    void sendOneOfClient(String msg, String clientName){
        int index = clients.indexOf(clientName);
        ClientHandler IntendedClient = clients.get(index);
        System.out.println(IntendedClient);
        System.out.println(msg);
    }


    //(g) Play random game roles among players

    /**
     * Play random game roles among players
     * @param PlayerName is name of the player to be randomly assigned a role
     */
    public void RoleDetermination(String PlayerName){
        Random randNum = new Random();
        HashMap<Integer, String> randomSet = new HashMap();

        String [] strList = new String[]{"GodFather", "SimpleMafia", "DoctorLector",
                "Mayor", "Citizen", "Doctor", "Psychologist", "Professional", "DieHard", "Detective"};
        int rand1 = (randNum.nextInt(10) + 1);
        randomSet.put(rand1, strList[0]);
        int i = 1;
        while(randomSet.size() != 10) {
            int rand2 = (randNum.nextInt(10) + 1);
            if (randomSet.containsKey(rand2))
                continue;
            else {
                randomSet.put(rand2, strList[i]);
                i++;
            }
        }
        int rndIndex = (randNum.nextInt(10) + 1);
        String role = randomSet.get(rndIndex);
        RoleToName.put(PlayerName, role);
        System.out.println(PlayerName + "Your role in the game is randomly selected and" + role);
    }


    /**
     * This method can remove a user from the game or allow her(him) to watch the continuation of the game
     * @throws IOException
     */
    public void removeInPlay() throws IOException {
        String myName = whatIsMyName(this);
        sendOneOfClient("You are not ready to play", myName);
        sendOneOfClient("You want to leave the game completely or" +
                " continue to be a spectator in the game? :" +
                "1.Quit the game" +
                "2.Watch the game", myName);
        int choose = input.read();
        if(choose == 1) {
            clientsName.remove(myName);
            clients.remove(this);
            RoleToName.remove(myName);
            clientsNameDeleted.put(myName, RoleToName.get(myName));
            sendToAll("User \"" + myName + "\" removed from game!!");
        }
        if(choose == 2) {
            WitnessClient.add(myName);
            sendOneOfClient(myName + "you will be able to view the game and in this case you can see" +
                    " the chat of the other players present", myName);
        }
    }


    /**
     *With this coupon method, all players are initially considered 0
     */
    public void CouponPlayerSet(){
        for(int i = 0; i < CouponPlayer.size(); i++){
            CouponPlayer.put(clientsName.get(i), index);
        }
    }


    /**
     * //By this method initially; The number of registered votes for all players present in the game is considered 0
     * @param Array is list of players
     */
    public void setVoting(ArrayList<String>Array){
        for(int i = 0; i < Array.size(); i++) {
            voting.put(Array.get(i), 0);
        }
    }

    /**
     * this method, voting at night (by Mafia) and voting during the day (by all players) can be done.
     * @param AppointmentTime is the maximum time required
     * @param votingTurn indicates whether voting takes place during the day or at night
     * @return result of the voting as the name of the winner of the voting
     * @throws IOException
     * @throws InterruptedException
     */
    public String Voting(int AppointmentTime, String votingTurn) throws IOException, InterruptedException {
        String name = whatIsMyName(this);
        if(votingTurn.equals("votingNight")) {
            if (name.equals("GodFather") || name.equals("SimpleMafia") || name.equals("DoctorLector"))
                sendOneOfClient("Please enter your desired name as a vote in " + AppointmentTime +
                        "seconds:", name);
        }
        if(votingTurn.equals("votingDay")){
            sendToAll("Please enter your desired name as a vote in " + AppointmentTime + "seconds:");
        }
        long start = System.currentTimeMillis();
        String slainName = input.readLine();

        Thread.sleep(AppointmentTime);

        long end = System.currentTimeMillis( );
        long dif = end - start;
        int Dif = (int) (dif * (0.001));

        //Check if the user has voted within the deadline
        if(Dif == AppointmentTime){
            if(slainName != null){
                ArraySlainName.add(slainName);
                if(votingTurn.equals("votingDay")){
                    setVoting(ArraySlainName);
                    if(voting.containsKey(slainName)){
                        int numberVote = voting.get(name);
                        int numberVoteRes = numberVote + 1;
                        voting.put(name, numberVoteRes);
                        //Ability to view each person's vote for all players in the game
                        sendToAll(slainName + " is the player's " + name );
                    }
                }
                sendOneOfClient("Thank you for voting :)", name);
            }
        }
        if(Dif > 5 || slainName == null) {
            sendOneOfClient("Your time is up" + "Difference is : " + Dif + "\n" +
                    "You spent more time than the deadline to submit the name." , name);
            int index = CouponPlayer.get(name);
            if(index == 2)
                removeInPlay();
            index++;
        }

        if(votingTurn.equals("votingNight")){
            String msg = "Dear Godfather :) The list below is the comments " +
                    "of all the Mafia for tonight's purpose" + "\n" +
                    "Please enter the final name of the person according to this list," +
                    " To do this, complete the following sentence and re-enter:" + "\n" +
                    "The name of the final victim ....";
            sendOneOfClient(msg, GFName);

            for(int i = 0; i < ArraySlainName.size(); i++){
                sendOneOfClient(ArraySlainName.get(i), GFName);
            }

            msgSlainName = input.readLine();
            slainNameRes = msgSlainName.split(" ")[6];
            sendOneOfClient("thank you GodFather, finish ;)", GFName);

            return slainNameRes;
        }
        if(votingTurn.equals("votingDay")){
            ArrayList<Integer>number = new ArrayList<>();
            for(Integer value: voting.values()){
                number.add(value);
            }
            Collections.sort(number);
            int index = clientsName.size();
            int MaxVote = number.get(index);
            for(int i = 0; i < ArraySlainName.size(); i++){
                String str = ArraySlainName.get(i);
                if(voting.get(str).equals(MaxVote))
                    return str;
            }
        }
        return "";
    }


    /**
     *The method of checking the winner and loser in this round of the game
     */
    public void EndOfGame(){
        ArrayList<String> LiveRoles = new ArrayList<>(RoleToName.values());
        int numberMafia = 0, numberCitizen = 0;
        for(int i = 0; i < clientsName.size(); i++){
            if(LiveRoles.get(i).equals("GodFather") || LiveRoles.get(i).equals("SimpleMafia") ||
                    LiveRoles.get(i).equals("DoctorLector"))
                numberMafia = numberMafia + 1;
            if(LiveRoles.get(i).equals("Mayor") || LiveRoles.get(i).equals("Citizen") ||
                    LiveRoles.get(i).equals("Doctor") || LiveRoles.get(i).equals("Psychologist") ||
                    LiveRoles.get(i).equals("Professional") || LiveRoles.get(i).equals("DieHard") ||
                    LiveRoles.get(i).equals("Detective"))
                numberCitizen = numberCitizen + 1;
        }
        if(numberMafia == 0){
            sendToAll("Winner of the game: Citizens group :)" + "\n" +
                    "Game loser: Mafia group :(");
        }
        else
            sendToAll("Winner of the game: Mafia group :)" + "\n" +
                    "Game loser: Citizens group :(");
    }

    /**
     * This method checks whether the user has not answered the requested questions more than 2 times or not
     * in which case she(he) can be removed from the game.
     * @param playerName is the name of the player whose coupon number is to be checked
     * @throws IOException
     */
    public void CheckPlayerCoupons(String playerName) throws IOException {
        int couponClient = CouponPlayer.get(playerName);
        if(couponClient == 2) {
            sendOneOfClient("You are not ready to play", playerName);
            removeInPlay();
        }
        else {
            couponClient = couponClient + 1;
            CouponPlayer.put(playerName, couponClient);
        }
    }
}
