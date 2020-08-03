package com.azhariharisalhamdi.githubuserapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.azhariharisalhamdi.githubuserapp.models.User;

public class detail_activity extends AppCompatActivity {
    public static final String USER_DATA_DETAIL = "user_data_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        //set activity titile
        setTitle("Detail User");

        //get parcel data intent
        User GithubUser = getIntent().getParcelableExtra(USER_DATA_DETAIL);

        //process image view from getting data
        ImageView mImageView;
        mImageView = (ImageView) findViewById(R.id.avatar);
        int avatarId = getResources().getIdentifier(GithubUser.getAvatar(), null, getPackageName());
        mImageView.setImageResource(avatarId);

        //get and set name of github user
        TextView nameTv;
        nameTv = (TextView) findViewById(R.id.name);
        nameTv.setText(GithubUser.getName());

        //get and set username of github user
        TextView userNameTv;
        userNameTv = (TextView) findViewById(R.id.username);
        userNameTv.setText(GithubUser.getUsername());

        //get and set repository of github user
        TextView repositoryTv;
        repositoryTv = (TextView) findViewById(R.id.repository);
        repositoryTv.setText(GithubUser.getPublic_repo());

        //get and set company of github user
        TextView companyTv;
        companyTv = (TextView) findViewById(R.id.company);
        companyTv.setText(GithubUser.getCompany());

        //get and set location of github user
        TextView locationTv;
        locationTv = (TextView) findViewById(R.id.location);
        locationTv.setText(GithubUser.getLocation());

        //get and set follower of github user
        TextView followerTv;
        followerTv = (TextView) findViewById(R.id.follower);
        followerTv.setText(GithubUser.getFollowers());

        //get and set following of github user
        TextView followingTv;
        followingTv = (TextView) findViewById(R.id.following);
        followingTv.setText(GithubUser.getFollowing());
    }
}
