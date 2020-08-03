package com.azhariharisalhamdi.githubuserapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azhariharisalhamdi.githubuserapp.DetailSelectedActivity;
import com.azhariharisalhamdi.githubuserapp.R;
import com.azhariharisalhamdi.githubuserapp.SearchActivity;
import com.azhariharisalhamdi.githubuserapp.callback.CustomOnItemClickListener;
import com.azhariharisalhamdi.githubuserapp.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FavoriteUserAdapter extends RecyclerView.Adapter<FavoriteUserAdapter.UserViewHolder> {
    private final ArrayList<User> listUser = new ArrayList<>();
    private final Activity activity;

    private String TAG = "favoriteUserAdapter";

    public FavoriteUserAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<User> getListUsers() {
        return listUser;
    }

    public void setListUsers(ArrayList<User> listUsers) {

        if (listUsers.size() > 0) {
            this.listUser.clear();
        }
        this.listUser.addAll(listUsers);

        notifyDataSetChanged();
    }

    public void addItem(User user) {
        this.listUser.add(user);
        notifyItemInserted(listUser.size() - 1);
    }

    public void updateItem(int position, User user) {
        this.listUser.set(position, user);
        notifyItemChanged(position, user);
    }

    public void removeItem(int position) {
        this.listUser.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listUser.size());
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_user_list, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.usernameTv.setText(listUser.get(position).getUsername());
//        int height = (int) Resources.getSystem().getDimension(R.dimen.avatar_size);
//        int width = (int) Resources.getSystem().getDimension(R.dimen.avatar_size);
        int height = 45;
        int width = 45;
        Glide.with(holder.itemView.getContext())
                .load(listUser.get(position).getAvatar())
                .apply(new RequestOptions().override(height, width))
                .into(holder.avatar);
        Log.d(TAG, ""+listUser.size());
        Log.d(TAG, ""+listUser.get(position).getFollowers());
        holder.followerTv.setText(""+listUser.get(position).getFollowers());
        holder.followingTv.setText(""+listUser.get(position).getFollowing());
        holder.userCard.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
//                Intent intent = new Intent(activity, DetailSelectedActivity.class);
//                intent.putExtra(DetailSelectedActivity.EXTRA_POSITION, position);
//                intent.putExtra(DetailSelectedActivity.EXTRA_USER, listUser.get(position));
//                activity.startActivityForResult(intent, DetailSelectedActivity.REQUEST_UPDATE);

                Intent moveWithObjectIntent = new Intent(activity, DetailSelectedActivity.class);
                moveWithObjectIntent.putExtra(DetailSelectedActivity.USER_DATA_DETAIL, listUser.get(position));
                activity.startActivity(moveWithObjectIntent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        final TextView usernameTv, followerTv, followingTv;
        ImageView avatar;
        final CardView userCard;

        UserViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            usernameTv = itemView.findViewById(R.id.username);
            followerTv = itemView.findViewById(R.id.follower);
            followingTv = itemView.findViewById(R.id.following);
            userCard = itemView.findViewById(R.id.user_card);
        }
    }
}
