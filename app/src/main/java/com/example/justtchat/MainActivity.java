package com.example.justtchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Adding main acitivity function
        Button btnStartButton = findViewById(R.id.btnStartButton);
        if(btnStartButton != null){
            btnStartButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            });
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.card), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public static class LoginActivity extends AppCompatActivity {

        private EditText etEmail, etPassword, etRoomCode;
        private Button btnLogin;
        private TextView tvSignUp;

        private FirebaseAuth mAuth;
        private FirebaseFirestore db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            etEmail = findViewById(R.id.etEmail);
            etPassword = findViewById(R.id.etPassword);
            etRoomCode = findViewById(R.id.etRoomCode);
            btnLogin = findViewById(R.id.btnLogin);
            tvSignUp = findViewById(R.id.tvSignUp);

            btnLogin.setOnClickListener(v -> {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String roomCodeInput = etRoomCode.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || roomCodeInput.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password, roomCodeInput);
                }
            });

            tvSignUp.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            });
        }

        private void loginUser(String email, String password, String roomCodeInput) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    String userId = mAuth.getCurrentUser().getUid();

                    db.collection("users").document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String savedRoomCode = documentSnapshot.getString("roomCode");

                                    if (savedRoomCode != null && savedRoomCode.equals(roomCodeInput)) {
                                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Incorrect Room Code.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show());

                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}