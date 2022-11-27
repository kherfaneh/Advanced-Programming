package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class NightDieHard extends PlayingNight{
    //filed
    private int CouponDoctor = 0;

    NightDieHard(Socket connectionSocket, ArrayList<ClientHandler> clients, ArrayList<String> clientsName) throws IOException {
        super(connectionSocket, clients, clientsName);
    }

    /**
     * Actions related to the role of the hardy at night; It is done in this method
     * @throws IOException
     */
    public void PlayingDieHard() throws IOException {
        sendOneOfClient("Hello DieHard, do you want to use your role?" + "\n" +
                "Yes or No?", DieHardName);
        String answer = input.readLine();
        if (answer.equals("No")) {
            sendOneOfClient("Ok, Thank You :)", DieHardName);
            sendToAll("DieHard is reluctant to use her(his) role in this round of play");
        }
        if(answer.equals("Yes")) {
            if (!(DieHardCoupons == 0 || DieHardCoupons == 1)) {
                sendOneOfClient("You have already used this feature, you are no longer able to " +
                        "receive inquiries from people removed from the narrator",DieHardName);
            }
            else{
                sendOneOfClient("The following is a list of names of people removed from the game:"
                        + "\n" + clientsNameDeleted.keySet() + "\n" + "Please enter a name from this list:", DieHardName);
                String InquiryDieHard = input.readLine();
                sendOneOfClient(clientsNameDeleted.get(InquiryDieHard), DieHardName);
            }
        }
    }
}
