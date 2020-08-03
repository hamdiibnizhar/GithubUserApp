package com.azhariharisalhamdi.consumerapp.rest;

import com.azhariharisalhamdi.consumerapp.models.FollowerUsers;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FollowerUserApi {
    @GET("/users/{username}/followers")
    Call<ArrayList<FollowerUsers>> UserGithub(@Path("username") String username);
}
