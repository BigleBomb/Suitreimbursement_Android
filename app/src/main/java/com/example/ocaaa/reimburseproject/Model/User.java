package com.example.ocaaa.reimburseproject.Model;

/**
 * Created by Asus on 6/18/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("nama")
    private String name;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("token")
    private String token;
    @SerializedName("limit")
    private int limit;
    @SerializedName("user_data")
    private List<User> user;

    public User(String id, String name, String username, String email, String token, int limit) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.token = token;
        this.limit = limit;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
