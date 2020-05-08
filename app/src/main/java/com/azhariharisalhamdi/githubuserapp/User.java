package com.azhariharisalhamdi.githubuserapp;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String username;
    private String name;
    private String avatar;
    private String company;
    private String location;
    private String repository;
    private String follower;
    private String following;

    public User(){}

    public User(String UserName, String Name, String Avatar, String Company, String Location, String Repository, String Follower, String Following){
        this.username = UserName;
        this.name = Name;
        this.avatar = Avatar;
        this.company = Company;
        this.location = Location;
        this.repository = Repository;
        this.follower = Follower;
        this.following = Following;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    public String getCompany() {
        return company;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
    public String getRepository() {
        return repository;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }
    public String getFollower() {
        return follower;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
    public String getFollowing() {
        return following;
    }

    protected User(Parcel in) {
        username = in.readString();
        name = in.readString();
        avatar = in.readString();
        company = in.readString();
        location = in.readString();
        repository = in.readString();
        follower = in.readString();
        following = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(company);
        dest.writeString(location);
        dest.writeString(repository);
        dest.writeString(follower);
        dest.writeString(following);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
