package com.azhariharisalhamdi.githubuserapp.rest;

import com.azhariharisalhamdi.githubuserapp.models.DetailUsers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DetailUserApi {
    @GET("/users/{username}")
    Call<DetailUsers> UserGithub(@Path("username") String username);
}
