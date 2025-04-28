package com.example.mymessenger;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    private List<User> users = new ArrayList<>();
    private OnUserClickListener onUserClickListener;

    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.users_item,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvUserInfo.setText(String.format(
                "%s %s, %s",
                user.getName(),
                user.getLastName(),
                user.getAge()
                )
        );

        int bgResId;
        if(user.getOnline()){
            bgResId = R.drawable.circle_green;
        }else{
            bgResId = R.drawable.circle_red;
        }
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), bgResId);
        holder.viewStatusOnline.setBackground(background);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onUserClickListener != null){
                    onUserClickListener.onUserClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    interface OnUserClickListener{
        void onUserClick(User user);
    }

    static class UsersViewHolder extends RecyclerView.ViewHolder{
        TextView tvUserInfo;
        View viewStatusOnline;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            viewStatusOnline = itemView.findViewById(R.id.viewStatusOnline);
            tvUserInfo = itemView.findViewById(R.id.tvUserInfo);
        }
    }
}
