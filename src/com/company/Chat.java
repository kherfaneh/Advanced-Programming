package com.company;

import java.io.*;
import java.util.ArrayList;

public class Chat implements Serializable {
    //filed
    private String teller;
    private String Content;

    //constructor
    public Chat(String teller, String Content){
        this.teller = teller;
        this.Content = Content;
    }

    /**
     *
     * @return data
     */
    public String getContent() {
        return Content;
    }

    /**
     *
     * @return teller
     */
    public String getTeller() {
        return teller;
    }

    /**
     *
     * @param data is Chat history
     */

    public void setContent(String data) {
        this.Content = data;
    }

    /**
     *
     * @param teller is name of the player who sent the message
     */
    public void setTeller(String teller) {
        this.teller = teller;
    }
    
}
