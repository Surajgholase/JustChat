package com.example.justtchat;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

        private TextView nameTextView, roleTextView;
        private ImageView profileImage;
        private Button editButton, logoutButton;

        private FirebaseAuth mAuth;
        private DatabaseReference mDatabase;
        private String userId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile); // XML file name

            // Initialize Firebase
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
            userId = mAuth.getCurrentUser().getUid();

            // UI References
            nameTextView = findViewById(R.id.textName);
            roleTextView = findViewById(R.id.textRole);
            profileImage = findViewById(R.id.profileImage);
            editButton = findViewById(R.id.editButton);
            logoutButton = findViewById(R.id.logoutButton);

            // Load User Data
            loadUserProfile();

            // Edit Profile Click
            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            });

            // Logout Click
            logoutButton.setOnClickListener(v -> {
                mAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        private void loadUserProfile() {
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue(String.class);
                    String role = snapshot.child("role").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                    nameTextView.setText(name);
                    roleTextView.setText(role);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this).load(imageUrl).into(profileImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
