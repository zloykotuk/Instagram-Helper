package com.example.instagramhelper;


import android.util.Log;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "https://www.instagram.com";
    private Retrofit mRetrofit;
    private CookieManager mCookieManager = null;

    private NetworkService(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient provideOkHttpClient() {
        mCookieManager = new CookieManager();
        mCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(mCookieManager);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.followRedirects(true);
        httpClient.addInterceptor(interceptor);
        httpClient.cookieJar(new JavaNetCookieJar(mCookieManager));

        return httpClient.build();
    }

    public static NetworkService getInstance(){
        if(mInstance == null){
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public JSONPlaceHolderApi getJSONApi(){
        return mRetrofit.create(JSONPlaceHolderApi.class);
    }

    private List<HttpCookie> getCookies() {
        if(mCookieManager == null)
            return null;
        else
            return mCookieManager.getCookieStore().getCookies();
    }

    public boolean isCookieManagerEmpty() {
        if(mCookieManager == null)
            return true;
        else
            return mCookieManager.getCookieStore().getCookies().isEmpty();
    }

    public String getCookieValue() {
        String cookieValue = new String();

        if(!isCookieManagerEmpty()) {
            for (HttpCookie eachCookie : getCookies())
                    cookieValue = cookieValue + String.format("%s=%s; ", eachCookie.getName(), eachCookie.getValue());

        }

        return cookieValue;
    }

    public String getCSRFToken() {
        String cookieValue = "";

        if(!isCookieManagerEmpty()) {
            for (HttpCookie eachCookie : getCookies()) {
                if (eachCookie.getName() == "csrftoken") {
                    cookieValue = eachCookie.getValue();
                }
                Log.i("222", cookieValue = eachCookie.getValue());
            }
        }

        return cookieValue;
    }
}

