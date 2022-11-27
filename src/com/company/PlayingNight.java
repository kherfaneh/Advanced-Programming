package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayingNight extends SharedData{

    //filed
    protected static String TargetRes;
    protected String savedLector;
    protected String saved;
    protected String psychologistGoal;
    protected int DieHardCoupons = 0;
    protected int DieHardLife = 0;
    protected HashMap<String, Integer> CouponPlayer = new HashMap();
    protected int index = 0;
    protected ArrayList<String>voterNameMafia = new ArrayList<>();


    //constructor
    PlayingNight(Socket connectionSocket, ArrayList<ClientHandler> clients, ArrayList<String> clientsName) throws IOException {
        super(connectionSocket, clients, clientsName);
    }

    //method
    public void CouponPlayerSet(){
        for(int i = 0; i < CouponPlayer.size(); i++){
            CouponPlayer.put(clientsName.get(i), index);
        }
    }
}
