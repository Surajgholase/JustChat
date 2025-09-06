package com.nip.justchat;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BottomAppBar bottomAppBar;
    private ChatFragment chatFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize fragments
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        // Find views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        // Bottom navigation item click listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_chat) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_layout, chatFragment)
                            .commit();
                } else if (id == R.id.nav_people) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_layout, profileFragment)
                            .commit();
                }
                return true;
            }
        });

        // Set default selected fragment
        bottomNavigationView.setSelectedItemId(R.id.nav_chat);

        // Hide BottomAppBar + FAB when keyboard opens
        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            View fab = findViewById(R.id.fab); // assuming you have FAB in layout

            if (keypadHeight > screenHeight * 0.15) {
                bottomAppBar.setVisibility(View.GONE);
                if (fab != null) fab.setVisibility(View.GONE);
            } else {
                bottomAppBar.setVisibility(View.VISIBLE);
                if (fab != null) fab.setVisibility(View.VISIBLE);
            }
        });
    }

    // Method called from ChatFragment to open profile
    public void openProfileFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame_layout, profileFragment)
                .commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_people);
    }
}
