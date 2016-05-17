package com.patrykkrawczyk.liveo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface INetwork {

    @FormUrlEncoded
    @POST("mobileLogin.php")
    Call<ResponseBody> login (@Field("username") String uername, @Field("pin") String pin);

    @FormUrlEncoded
    @POST("data.php")
    Call<ResponseBody> data (@Field("id") String id, @Field("latitude") String latitude,
                             @Field("longitude") String longitude, @Field("heartRate") String heartRate,
                             @Field("accX") String accX, @Field("accY") String accY,
                             @Field("accZ") String accZ);

    @FormUrlEncoded
    @POST("getDriver.php")
    Call<ResponseBody> driver (@Field("id") String id);

    @FormUrlEncoded
    @POST("modify.php")
    Call<ResponseBody> modify (@Field("id") String id, @Field("firstName") String firstName,
                               @Field("lastName") String lastName, @Field("registerNumber") String registerNumber,
                               @Field("gender") String gender, @Field("ageGroup") String ageGroup);

}