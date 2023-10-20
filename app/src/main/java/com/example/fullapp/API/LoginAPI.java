package com.example.fullapp.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginAPI {
    @FormUrlEncoded
    @POST("auth/login.php")
    Call<ResponseBody> login(@Field("email") String email,
                             @Field("password") String password);
}
