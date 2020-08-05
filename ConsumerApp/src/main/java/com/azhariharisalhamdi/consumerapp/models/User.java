package com.azhariharisalhamdi.consumerapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String username;
    private String avatar;
    private String url;
    private String name;
    private String company;
    private String location;
    private String blog;
    private int public_repo;
    private int public_gists;
    private int followers;
    private int following;
    private int id;

    public User(){}

    public User(String UserName, String Name, String Avatar, String url, String Company, String Location, String blog, int public_repo, int public_gists, int followers, int Following){
        this.username = UserName;
        this.name = Name;
        this.avatar = Avatar;
        this.url = url;
        this.company = Company;
        this.location = Location;
        this.blog = blog;
        this.public_repo = public_repo;
        this.public_gists = public_gists;
        this.followers = followers;
        this.following = Following;
    }

    public User(int id, String UserName, String Avatar, int followers, int Following){
        this.id = id;
        this.username = UserName;
        this.avatar = Avatar;
        this.followers = followers;
        this.following = Following;
    }

    public User(String UserName, String Avatar, int followers, int Following){
        this.username = UserName;
        this.avatar = Avatar;
        this.followers = followers;
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

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
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

    public void setBlog(String blog) {
        this.blog = blog;
    }
    public String getBlog() {
        return blog;
    }

    public void setPublic_repo(int public_repo) {
        this.public_repo = public_repo;
    }
    public int getPublic_repo() {
        return public_repo;
    }

    public void setPublic_gists(int public_gists) {
        this.public_gists = public_gists;
    }
    public int getPublic_gists() {
        return public_gists;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
    public int getFollowers() {
        return followers;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
    public int getFollowing() {
        return following;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    protected User(Parcel in) {
        username = in.readString();
        name = in.readString();
        avatar = in.readString();
        url = in.readString();
        company = in.readString();
        location = in.readString();
        blog = in.readString();
        public_repo = in.readInt();
        public_gists = in.readInt();
        followers = in.readInt();
        following = in.readInt();
        id = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(url);
        dest.writeString(company);
        dest.writeString(location);
        dest.writeString(blog);
        dest.writeInt(public_repo);
        dest.writeInt(public_gists);
        dest.writeInt(followers);
        dest.writeInt(following);
        dest.writeInt(id);
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
