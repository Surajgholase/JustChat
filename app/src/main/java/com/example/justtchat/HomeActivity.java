package com.example.justtchat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;

    BottomNavigationView bottomNavigationView;

    ChatFragment chatFragment;
    ProfileFragment profileFragment;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.nav_chat){
                    getSupportFragmentManager().beginTransaction ().replace(R.id.main_frame_layout, chatFragment).commit();
                }
                if(menuItem.getItemId()==R.id.nav_people){
                    getSupportFragmentManager().beginTransaction ().replace(R.id.main_frame_layout, profileFragment).commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_chat);

//        chatRecyclerView = findViewById(R.id.chatRecyclerView);
//
//        //Sample message
//        messageList = new ArrayList<>();
//        messageList.add(new Message("Hey, are you free this weeekend?",false,"11.58"));
//        messageList.add(new Message("I guess I am", true, "11:59"));
//        messageList.add(new Message("We are planning to go on a trip", false, "12:38"));
//        messageList.add(new Message("All of us, some friends from work might come too", false, "01:44"));
//
//        chatAdapter = new ChatAdapter(messageList);
//        chatRecyclerView.setAdapter(chatAdapter);
//        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));



    }


}
