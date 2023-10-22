package com.example.fullapp.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetAPI {
    @GET("actions/get-user-profile.php")
    Call<ResponseBody> getProfile(
            @Query("email") String email
    );
}
