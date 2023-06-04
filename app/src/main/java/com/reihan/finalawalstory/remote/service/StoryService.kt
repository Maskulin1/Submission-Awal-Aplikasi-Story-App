package com.reihan.finalawalstory.remote.service

import com.reihan.finalawalstory.remote.data.DetailStoryResponse
import com.reihan.finalawalstory.remote.data.GetStoryResponse
import com.reihan.finalawalstory.remote.data.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StoryService {
    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<StoryResponse>

    @GET("stories")
    fun getAllStories(): Call<GetStoryResponse>

    @GET("stories/{id}")
    fun getDetailStory(@Path("id")id:String): Call<DetailStoryResponse>

}