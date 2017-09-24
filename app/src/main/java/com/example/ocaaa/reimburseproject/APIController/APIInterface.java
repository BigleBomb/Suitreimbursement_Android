package com.example.ocaaa.reimburseproject.APIController;

/**
 * Created by Asus on 7/20/2017.
 */

import com.example.ocaaa.reimburseproject.Model.ProjectList;
import com.example.ocaaa.reimburseproject.Model.Reimburse;
import com.example.ocaaa.reimburseproject.Model.ReimburseList;
import com.example.ocaaa.reimburseproject.Model.ReimburseResponse;
import com.example.ocaaa.reimburseproject.Model.User;
import com.example.ocaaa.reimburseproject.Model.UserResponse;
import com.squareup.picasso.Request;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface APIInterface {
    @POST("user/login")
    @FormUrlEncoded
    Call<UserResponse> loginUser(@Field("email") String email,
                                 @Field("password") String password);

    @GET("user/get/{id}")
    Call<User> getUser(@Path("id") int id,
                       @Query("token") String token);

    @GET("project/getbyuserid/{id}")
    Call<ProjectList> getProject(@Path("id") String id,
                                 @Query("token") String token);

    @POST("reimburse/new")
    @Multipart
    Call<ReimburseResponse> addReimburse(@Part("user_id") RequestBody uid,
                                         @Part("project_id") RequestBody pid,
                                         @Part("date") RequestBody date,
                                         @Part("category") RequestBody category,
                                         @Part("cost") RequestBody cost,
                                         @Part MultipartBody.Part file,
                                         @Part("details") RequestBody details,
                                         @Query("token") String token);

    @GET("/reimburse/get/{id}")
    Call<ReimburseResponse> getReimburse(@Path("id") String id,
                                         @Query("token") String token);

    @GET("/reimburse/delete/{id}")
    Call<ReimburseResponse> deleteReimburse(@Path("id") String id,
                                         @Query("token") String token);

    @GET("/reimburse/getfromprojectbyuserid/{pid}/{id}")
    Call<ReimburseList> getReimburseFromProjectByUserId(@Path("pid") String pid,
                                                              @Path("id") String uid,
                                                              @Query("token") String token);







}
