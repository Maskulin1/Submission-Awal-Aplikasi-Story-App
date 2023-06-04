package com.reihan.finalawalstory.remote.service

import com.reihan.finalawalstory.remote.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email")email: String,
        @Field("password")password : String
    ) : Call<LoginResponse>
}