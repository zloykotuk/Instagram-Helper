package com.example.instagramhelper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class LoginUser {

    private String password;
    private String username;


    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class LoginResponse {
    @SerializedName("csrftoken")
    @Expose
    public static String CSRFToken = "";

    public String getToken() {
        return CSRFToken;
    }


}
