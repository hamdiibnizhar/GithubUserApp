package com.azhariharisalhamdi.githubuserapp.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azhariharisalhamdi.githubuserapp.R;
import com.azhariharisalhamdi.githubuserapp.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.ListViewHolder>{
    private ArrayList<User> listUser;

    public ListUsersAdapter(ArrayList<User> list) {
        this.listUser = list;
    }



    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        User user = listUser.get(position);
//        int height = (int) Resources.getSystem().getDimension(R.dimen.avatar_size);
//        int width = (int) Resources.getSystem().getDimension(R.dimen.avatar_size);
        int height = 45;
        int width = 45;
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar())
                .apply(new RequestOptions().override(height, width))
                .into(holder.imgPhoto);
        holder.tvName.setText(user.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listUser.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.avatar);
            tvName = itemView.findViewById(R.id.username);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }
}
