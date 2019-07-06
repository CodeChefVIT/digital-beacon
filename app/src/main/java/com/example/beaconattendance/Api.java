package com.example.beaconattendance;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
   @POST("employees")
    Call<ResponseBody> employees(
           @Field("id")String id,
           @Field("firstname")String firstname,
           @Field("lastname")String lastname,
           @Field("emp_id")String emp_id
   );

}
