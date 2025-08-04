package com.example.justtchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<Message> messageList;

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).isSentByMe()) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);
        }

        void bind(Message message) {
            messageText.setText(message.getText());
            messageTime.setText(message.getTime());
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);
        }

        void bind(Message message) {
            messageText.setText(message.getText());
            messageTime.setText(message.getTime());
        }
    }
}
