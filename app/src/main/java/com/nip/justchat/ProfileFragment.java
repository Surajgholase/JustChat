package com.nip.justchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private LinearLayout btnEditProfile, roomCodeBtn, logoutBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private StorageReference storageRef;
    private ProgressDialog progressDialog;

    private ActivityResultLauncher<String> pickImageLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);

        btnEditProfile = view.findViewById(R.id.etName);
        roomCodeBtn = view.findViewById(R.id.etRoomCode);
        logoutBtn = view.findViewById(R.id.etLogout);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        }

        storageRef = FirebaseStorage.getInstance().getReference("profile_images");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");

        // Activity result launcher for picking images
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uploadProfileImage(uri);
                    }
                });

        btnEditProfile.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        roomCodeBtn.setOnClickListener(v ->
                Toast.makeText(getContext(), "Room Code clicked", Toast.LENGTH_SHORT).show()
        );

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        // Load user profile
        loadUserProfile();

        return view;
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);

            ref.get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String profileUrl = snapshot.child("profileImageUrl").getValue(String.class);

                    if (name != null) profileName.setText(name);
                    if (email != null) profileEmail.setText(email);
                    if (profileUrl != null && !profileUrl.isEmpty()) {
                        Picasso.get().load(profileUrl).into(profileImage);
                    }
                } else {
                    Toast.makeText(getContext(), "No user data found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e ->
                    Toast.makeText(getContext(), "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage(@NonNull Uri fileUri) {
        progressDialog.show();

        String fileName = UUID.randomUUID().toString();
        StorageReference fileRef = storageRef.child(fileName);

        UploadTask uploadTask = fileRef.putFile(fileUri);
        uploadTask.addOnSuccessListener(taskSnapshot ->
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        DatabaseReference userNode = FirebaseDatabase.getInstance().getReference("users").child(uid);
                        userNode.child("profileImageUrl").setValue(downloadUrl)
                                .addOnSuccessListener(aVoid -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                                    Picasso.get().load(downloadUrl).into(profileImage); // Update immediately
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Failed to save profile URL", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "User not signed in.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed to get download URL.", Toast.LENGTH_SHORT).show();
                })
        ).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
