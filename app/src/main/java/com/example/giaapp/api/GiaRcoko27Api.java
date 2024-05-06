package com.example.giaapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GiaRcoko27Api {
    @GET("MobileApp/GetTest/{id}")
    public Call<Post> getPostWithID(@Path("id") int id);

    @POST("Login/CreateApiKey")
    public Call<LoginResult> userAuth(@Body LoginModel model);

    @POST("Login/GetActualInfo")
    public Call<LoginResult> updateUserInfo(@Header("API-Key") String apiKey);
}
