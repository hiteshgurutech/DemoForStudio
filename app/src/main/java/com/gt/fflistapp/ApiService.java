package com.gt.fflistapp;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v1/users/self/follows/")
    Call<User> getList(
            @Query("access_token") String accessToken);

}
