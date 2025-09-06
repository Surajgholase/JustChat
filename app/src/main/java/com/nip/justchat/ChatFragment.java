package com.nip.justchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    public static String CURRENT_ROOM = ""; // set from LoginActivity

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageView sendButton;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    private FirebaseFirestore db;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUserId);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> sendMessage());

        loadMessages();

        return view;
    }

    private void loadMessages() {
        if (CURRENT_ROOM.isEmpty()) return;

        db.collection("rooms")
                .document(CURRENT_ROOM)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (snapshots != null) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Message message = dc.getDocument().toObject(Message.class);
                                messageList.add(message);
                                messageAdapter.notifyItemInserted(messageList.size() - 1);
                                chatRecyclerView.scrollToPosition(messageList.size() - 1);
                            }
                        }
                    }
                });
    }

    private void sendMessage() {
        String text = messageEditText.getText().toString().trim();
        if (text.isEmpty() || CURRENT_ROOM.isEmpty()) return;

        Message msg = new Message();
        msg.setText(text);
        msg.setSenderId(currentUserId);
        msg.setTimestamp(System.currentTimeMillis());

        db.collection("rooms")
                .document(CURRENT_ROOM)
                .collection("messages")
                .add(msg)
                .addOnSuccessListener(docRef -> messageEditText.setText(""));
    }
}
