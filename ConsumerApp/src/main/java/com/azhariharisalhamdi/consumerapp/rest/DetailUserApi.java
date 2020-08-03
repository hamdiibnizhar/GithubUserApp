package com.azhariharisalhamdi.consumerapp.rest;

import com.azhariharisalhamdi.consumerapp.models.DetailUsers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DetailUserApi {
    @GET("/users/{username}")
    Call<DetailUsers> UserDetailedGithub(@Path("username") String username);
}
