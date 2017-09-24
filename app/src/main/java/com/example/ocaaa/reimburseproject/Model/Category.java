package com.example.ocaaa.reimburseproject.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 8/4/2017.
 */

public class Category {
    @SerializedName("name")
    private String name;
    @SerializedName("limit")
    private int limit;
    @SerializedName("message")
    private String message;

    public Category(String name, int limit) {
        this.name = name;
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
