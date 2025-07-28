package com.example.justtchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etConfirmPassword, etRoomCode;
    Button btnSignUp;
    TextView tvLogin;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etRoomCode = findViewById(R.id.etRoomCode);
        tvLogin = findViewById(R.id.tvLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String roomCode = etRoomCode.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || roomCode.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(name, email, password, roomCode);
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser(String name, String email, String password, String roomCode) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    String userId = user.getUid();

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("email", email);
                    userMap.put("roomCode", roomCode);

                    db.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(SignUpActivity.this, "User Registered!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            } else {
                String errorMsg = task.getException().getMessage();
                if (errorMsg != null && errorMsg.contains("email address is already in use")) {
                    Toast.makeText(SignUpActivity.this, "Email already registered. Please log in instead.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(SignUpActivity.this, "Registration Failed: " + errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}