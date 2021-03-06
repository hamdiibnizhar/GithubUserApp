package com.azhariharisalhamdi.consumerapp.rest;

import com.azhariharisalhamdi.consumerapp.models.FollowingUsers;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FollowingUserApi {
    @GET("/users/{username}/following")
    Call<ArrayList<FollowingUsers>> UserGithub(@Path("username") String username);
}
