package com.example.ocaaa.reimburseproject.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 7/22/2017.
 */

public class UserResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private String status;
    @SerializedName("user_data")
    private User userData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
