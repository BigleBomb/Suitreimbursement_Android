package com.example.ocaaa.reimburseproject.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Asus on 6/18/2017.
 */

public class Project implements Serializable{
    @SerializedName("id")
    private String id;
    @SerializedName("created_at")
    private String date;
    @SerializedName("project_name")
    private String project_name;
    @SerializedName("total_cost")
    private int total_cost;
    @SerializedName("details")
    private String details;

    public Project(String id, String date, String project_name, int total_cost, String details) {
        this.id = id;
        this.date = date;
        this.project_name = project_name;
        this.total_cost = total_cost;
        this.details = details;
    }

    public Project() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(int total_cost) {
        this.total_cost = total_cost;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}
