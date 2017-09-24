package com.example.ocaaa.reimburseproject.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 7/23/2017.
 */

public class ReimburseResponse {
    @SerializedName("success")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private Reimburse reimburse;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Reimburse getReimburse() {
        return reimburse;
    }
}
