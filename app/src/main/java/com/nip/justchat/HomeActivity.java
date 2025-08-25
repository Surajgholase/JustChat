package com.nip.justchat;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    BottomAppBar bottomAppBar;
    ChatFragment chatFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_chat) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_layout, chatFragment)
                            .commit();
                }
                if (menuItem.getItemId() == R.id.nav_people) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_layout, profileFragment)
                            .commit();
                }
                return true;
            }
        });

        // Default fragment
        bottomNavigationView.setSelectedItemId(R.id.nav_chat);

        // Hide bottom bar + FAB when keyboard is open
        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15) {
                bottomAppBar.setVisibility(View.GONE);
                findViewById(R.id.fab).setVisibility(View.GONE);
            } else {
                bottomAppBar.setVisibility(View.VISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });
    }

    // Called from ChatFragment
    public void openProfileFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame_layout, profileFragment)
                .commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_people);
    }
}
