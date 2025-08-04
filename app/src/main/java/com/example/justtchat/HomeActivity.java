package com.example.justtchat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        //Sample message
        messageList = new ArrayList<>();
        messageList.add(new Message("Hey, are you free this weeekend?",false,"11.58"));
        messageList.add(new Message("I guess I am", true, "11:59"));
        messageList.add(new Message("We are planning to go on a trip", false, "12:38"));
        messageList.add(new Message("All of us, some friends from work might come too", false, "01:44"));

        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
