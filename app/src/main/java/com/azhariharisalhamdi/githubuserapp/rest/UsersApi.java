package com.azhariharisalhamdi.githubuserapp.rest;

import com.azhariharisalhamdi.githubuserapp.models.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersApi {
    @GET("/search/users?")
    Call<Users> UserGithub(@Query("q") String username);
}
