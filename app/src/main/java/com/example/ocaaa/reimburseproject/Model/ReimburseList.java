package com.example.ocaaa.reimburseproject.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Asus on 7/22/2017.
 */

public class ReimburseList {
    @SerializedName("result")
    private ArrayList<Reimburse> ReimburseList;
    @SerializedName("success")
    private String status;
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Reimburse> getReimburseList() {
        return ReimburseList;
    }

    public void setReimburseList(ArrayList<Reimburse> reimburseList) {
        ReimburseList = reimburseList;
    }

//    public String getResult() {
//        return result;
//    }
//
//    public void setResult(String result) {
//        this.result = result;
//    }
}
