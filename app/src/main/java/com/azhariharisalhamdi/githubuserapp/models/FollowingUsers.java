package com.azhariharisalhamdi.githubuserapp.models;

import com.google.gson.annotations.SerializedName;

public class FollowingUsers {
    @SerializedName("login")
    String username;
    @SerializedName("avatar_url")
    String avatar_url;

    public void setUsername(String username){
        this.username = username;
    }

    public  String getUsername(){
        return username;
    }

    public void setAvatar_url(String avatar_url){
        this.avatar_url = avatar_url;
    }

    public  String getAvatar_url(){
        return avatar_url;
    }
}
