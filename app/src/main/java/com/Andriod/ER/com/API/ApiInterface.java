package com.Andriod.ER.com.API;

import com.Andriod.ER.com.Model.LoginResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

public interface ApiInterface {

    // For POST request
    @FormUrlEncoded    // annotation that used with POST type request
    @POST("/mobile/register")
    void register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("country") String country,Callback<JsonObject> callback);


    @FormUrlEncoded
    @POST("/mobile/login")
    void login(
            @Field("email") String email,
            @Field("password") String password, Callback<JsonObject> callback);

    @POST("/mobile/user")
    @Headers({ "Content-Type: application/xml; charset=Utf-8"})
    void user(@Header("Authorization") String auth, Callback<JsonObject> callback);


    @FormUrlEncoded
    @POST("/mobile/reset-password")
    void reset_password(
            @Field("email") String email,
            Callback<JSONObject> callback);
    @FormUrlEncoded
    @POST("/mobile/change-password")
    //@Headers({ "Content-Type: application/x-www-form-urlencoded; charset=Utf-8"})
    void change_password(
            @Header("Authorization") String auth,
            @Field("newPassword") String newPassword,
            Callback<JsonObject> callback);


    @FormUrlEncoded
    @POST("/batteries/store")
    void storeBattery(
            @Field("name") String name,
            @Field("password") String password,
            @Field("sparkDate") String sparkDate,
            @Field("normalDate") String normalDate,
            @Field("reserveTank") Boolean reserveTank,
            @Field("userId") String userId,
            @Field("userName") String userName,
            @Field("dateToggler") Boolean dateToggler,
            Callback<JsonObject> callback);


    @FormUrlEncoded
    @POST("/languages/show")
    void getLanguages(
            @Field("languageId") String languageId,
            Callback<JsonObject> callback);


    @GET("/mobile/languages")
    void getLanguagesList(Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/batteries/user-batteries")
    void getUserBatteries(
            @Field("userId") String userId,
            Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/batteries/update")
    void updateBattery(
            @Field("name") String name,
            @Field("password") String password,
            @Field("sparkDate") String sparkDate,
            @Field("normalDate") String normalDate,
            @Field("reserveTank") Boolean reserveTank,
            @Field("userId") String userId,
            @Field("userName") String userName,
            @Field("dateToggler") Boolean dateToggler,
            @Field("batteryId") String batteryId,
            Callback<JsonObject> callback);


}
