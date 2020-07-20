package com.azhariharisalhamdi.githubuserapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Users {
    @SerializedName("total_count")
    int total_count;
    @SerializedName("items")
    ArrayList<Item> items;

    public void setTotal_count(int total_count){
        this.total_count = total_count;
    }

    public int getTotal_count(){
        return total_count;
    }

    public void setItems(ArrayList<Item> items){
        this.items = items;
    }

    public ArrayList<Item> getItems(){
        return items;
    }
}
