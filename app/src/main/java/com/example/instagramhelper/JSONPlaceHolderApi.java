package com.example.instagramhelper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi{
    @Headers({
            "User-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36",
            "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,zh-TW;q=0.6",
            "cache-control: max-age=0",
            "content-type: application/x-www-form-urlencoded"
    })
    @GET("/{username}/?__a=1")
    Call<Post> getPostWithID(@Path("username") String id, @Header("Cookie") String Cookies);


    @Headers({
            "X-Instagram-AJAX: e0f5f1d27e2c",
            "X-IG-App-ID: 936619743392459",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept: */*",
            "X-Requested-With: XMLHttpRequest"

    })
    @POST("/accounts/login/ajax/")
    Call<LoginResponse> login(@Body LoginUser loginUser, @Header("X-CSRFToken") String CSRFToken, @Header("Cookie") String Cookies);
}

