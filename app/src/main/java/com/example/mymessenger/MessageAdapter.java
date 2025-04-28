package com.example.mymessenger;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private static final int VIEW_TYPE_MY_MESSAGE=1;
    private static final int VIEW_TYPE_OTHER_MESSAGE=-1;
    private String currentUserId;
    private List<Message> messages = new ArrayList<>();

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        Log.d("setMessages", "setMessages: "+messages.toString());
        notifyDataSetChanged();
    }

    public MessageAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        if(viewType == VIEW_TYPE_MY_MESSAGE){
            layoutResId = R.layout.my_message;
        }else{
            layoutResId = R.layout.other_message_item;
        }
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                layoutResId,
                parent,
                false
        ));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tvMessage.setText(message.getText());
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(message.getSenderId().equals(currentUserId)){
            return VIEW_TYPE_MY_MESSAGE;
        }else{
            return VIEW_TYPE_OTHER_MESSAGE;
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder{
        private TextView tvMessage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
