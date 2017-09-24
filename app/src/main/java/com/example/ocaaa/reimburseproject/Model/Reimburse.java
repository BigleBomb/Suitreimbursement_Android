package com.example.ocaaa.reimburseproject.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import com.example.ocaaa.reimburseproject.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Asus on 7/8/2017.
 */

public class Reimburse implements Serializable, Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("project_id")
    private String project_id;
    @SerializedName("created_at")
    private String date;
    @SerializedName("category")
    private String category;
    @SerializedName("cost")
    private int cost;
    @SerializedName("picture")
    private String picture;
    @SerializedName("status")
    private int status;
    @SerializedName("details")
    private String details;
    @SerializedName("reason")
    private String reason;
    @SerializedName("image")
    private String image;

    Bitmap Image;

    public Reimburse(String id, String user_id, String project_id, String date, String category, int cost, String image, int status, String details, String reason) {
        this.id = id;
        this.user_id = user_id;
        this.project_id = project_id;
        this.date = date;
        this.category = category;
        this.cost = cost;
        this.image = image;
        this.status = status;
        this.details = details;
        this.reason = reason;
    }

    public Reimburse(String id, String date, String category, int cost, String image) {

        this.id = id;
        this.date = date;
        this.category = category;
        this.cost = cost;
        this.image = image;
    }

    protected Reimburse(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        project_id = in.readString();
        date = in.readString();
        category = in.readString();
        cost = in.readInt();
        picture = in.readString();
        status = in.readInt();
        details = in.readString();
        reason = in.readString();
        image = in.readString();
        Image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Reimburse> CREATOR = new Creator<Reimburse>() {
        @Override
        public Reimburse createFromParcel(Parcel in) {
            return new Reimburse(in);
        }

        @Override
        public Reimburse[] newArray(int size) {
            return new Reimburse[size];
        }
    };

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCategoryText() {
        switch(category){
            case "Transportasi":
                return R.string.categoryTransportation;
            case "Konsumsi":
                return R.string.categoryConsumption;
            case "Akomodasi":
                return R.string.categoryAccommodation;
        }
        return 0;
    }

    public int getHeaderColor() {
        switch(category){
            case "Transportasi":
                return R.color.Transportasi;
            case "Konsumsi":
                return R.color.Konsumsi;
            case "Akomodasi":
                return R.color.Akomodasi;
        }
        return 0;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public int getStatusText(){
        switch(status){
            case 0:
                return R.string.reimburse_status_pending;
            case 1:
                return R.string.reimburse_status_accepted;
            case 2:
                return R.string.reimburse_status_rejected;
        }
        return 3;
    }

    public int getColor(){
        switch(status){
            case 0:
                return R.color.statusPending;
            case 1:
                return R.color.statusAccepted;
            case 2:
                return R.color.statusRejected;
        }
        return 0;
    }

    public int getIcon(){
        switch(category){
            case "Transportasi":
                return R.drawable.ic_directions_car;
            case "Konsumsi":
                return R.drawable.ic_free_breakfast;
            case "Akomodasi":
                return R.drawable.ic_hotel;
        }
        return 0;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getImageString(){
        return image;
    }

    public Bitmap getImage() {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return Image;
    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(project_id);
        dest.writeString(date);
        dest.writeString(category);
        dest.writeInt(cost);
        dest.writeString(picture);
        dest.writeInt(status);
        dest.writeString(details);
        dest.writeString(reason);
        dest.writeString(image);
        dest.writeParcelable(Image, flags);
    }
}
