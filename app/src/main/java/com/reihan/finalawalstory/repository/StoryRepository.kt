package com.reihan.finalawalstory.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reihan.finalawalstory.remote.data.DetailStoryResponse
import com.reihan.finalawalstory.remote.data.GetStoryResponse
import com.reihan.finalawalstory.remote.data.ListStory
import com.reihan.finalawalstory.remote.data.Story
import com.reihan.finalawalstory.remote.data.StoryResponse
import com.reihan.finalawalstory.remote.service.StoryService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository (private val storyService: StoryService){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading
    fun addStory(imageData : MultipartBody.Part, description: RequestBody): LiveData<StoryResponse> {
        _isLoading.value = true
        val liveData = MutableLiveData<StoryResponse>()
        storyService.addStory(imageData, description).enqueue(object: Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null ){
                    liveData.value = response.body()
                }else{
                    Log.d("AddStory", response.message())
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("AddStory", t.message.toString())
            }

        } )

        return liveData
    }

    fun fetchStory(): LiveData<List<ListStory>> {
        _isLoading.value = true
        val liveData = MutableLiveData<List<ListStory>>()
        storyService.getAllStories().enqueue(object : Callback<GetStoryResponse> {
            override fun onResponse(
                call: Call<GetStoryResponse>,
                response: Response<GetStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null){
                    liveData.value = response.body()?.listStory
                }else {
                    Log.d("fetchStory", response.message())
                }
            }

            override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
                Log.d("fetchStory", t.message.toString())
            }

        })

        return liveData
    }

    fun getDetailStory(id : String): LiveData<Story> {
        _isLoading.value = true
        val liveData = MutableLiveData<Story>()
        storyService.getDetailStory(id).enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    Log.d("getDetailStory", response.body().toString())
                    liveData.value = response.body()?.story
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("getDetailStory", t.message.toString())
            }
        })
        return liveData
    }
}