package com.azhariharisalhamdi.githubuserapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailUsers {
    @SerializedName("login")
    String username;
    @SerializedName("avatar_url")
    String avatar_url;
    @SerializedName("url")
    String url;
    @SerializedName("name")
    String name;
    @SerializedName("company")
    String company;
    @SerializedName("blog")
    String blog;
    @SerializedName("location")
    String location;
    @SerializedName("public_repos")
    int public_repos;
    @SerializedName("public_gists")
    int public_gists;
    @SerializedName("followers")
    int followers;
    @SerializedName("following")
    int following;

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setAvatar_url(String avatar_url){
        this.avatar_url = avatar_url;
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

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setCompany(String company){
        this.company = company;
    }

    public String getCompany(){
        return company;
    }

    public void setBlog(String blog){
        this.blog = blog;
    }

    public String getBlog(){
        return blog;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
    }

    public void setPublic_repos(int public_repos){
        this.public_repos = public_repos;
    }

    public int getPublic_repos(){
        return public_repos;
    }

    public void setPublic_gists(int public_gists){
        this.public_gists = public_gists;
    }

    public int getPublic_gists(){
        return public_gists;
    }

    public void setFollowers(int followers){
        this.followers = followers;
    }

    public int getFollowers(){
        return followers;
    }

    public void setFollowing(int following){
        this.following = following;
    }

    public int getFollowing(){
        return following;
    }

}
