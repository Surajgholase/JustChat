package com.nip.justchat;

public class Message {
    private String text;
    private String senderId;
    private long timestamp;

    public Message() {}

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
