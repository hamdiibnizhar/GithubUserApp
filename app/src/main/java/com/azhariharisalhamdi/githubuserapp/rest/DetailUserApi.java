package com.azhariharisalhamdi.githubuserapp.rest;

import com.azhariharisalhamdi.githubuserapp.models.DetailUsers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DetailUserApi {
    @GET("/users/{username}")
    Call<DetailUsers> UserDetailedGithub(@Path("username") String username);
}
