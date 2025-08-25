package com.nip.justchat;

public class Message {
    private String text;
    private boolean isSentByMe;
    private String time;

    public Message(String text, boolean isSentByMe, String time){
        this.text = text;
        this.isSentByMe = isSentByMe;
        this.time = time;
    }
    public String getText(){
        return text;
    }
    public boolean isSentByMe(){
        return isSentByMe;
    }
    public String getTime(){
        return time;
    }

}
