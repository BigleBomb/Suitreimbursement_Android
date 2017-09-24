package com.example.ocaaa.reimburseproject.Model;

/**
 * Created by Asus on 7/21/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProjectList {
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private ArrayList<Project> ProjectList;
    @SerializedName("success")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Project> getProjectList() {
        return ProjectList;
    }

    public void setProjectList(ArrayList<Project> projectList) {
        ProjectList = projectList;
    }
}
