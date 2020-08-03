package com.azhariharisalhamdi.consumerapp.rest;

import com.azhariharisalhamdi.consumerapp.models.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersApi {
    @GET("/search/users?")
    Call<Users> UserGithub(@Query("q") String username);
}
