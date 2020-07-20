package com.azhariharisalhamdi.githubuserapp.rest;

import com.azhariharisalhamdi.githubuserapp.models.FollowerUsers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FollowerUserApi {
    @GET("/users/{username}/followers")
    Call<FollowerUsers> UserGithub(@Path("username") String username);
}
