package com.azhariharisalhamdi.consumerapp.models;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("login")
    String username;
    @SerializedName("avatar_url")
    String avatar_url;
    @SerializedName("url")
    String url;
    @SerializedName("followers_url")
    String followers_url;
    @SerializedName("following_url")
    String following_url;

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setAvatar_url(String avatar_url){
        this.username = avatar_url;
    }

    public String getAvatar_url(){
        return avatar_url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public void setFollowers_url(String followers_url){
        this.followers_url = followers_url;
    }

    public String getFollowers_url(){
        return followers_url;
    }

    public void setFollowing_url(String following_url){
        this.following_url = following_url;
    }

    public String getFollowing_url(){
        return following_url;
    }
}
