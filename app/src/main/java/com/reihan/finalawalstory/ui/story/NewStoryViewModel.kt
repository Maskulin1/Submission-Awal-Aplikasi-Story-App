package com.reihan.finalawalstory.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.finalawalstory.remote.data.StoryResponse
import com.reihan.finalawalstory.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NewStoryViewModel(private val storyRepository: StoryRepository) : ViewModel(){
    private val _addStoryResult = MutableLiveData<StoryResponse>()
    val addStoryResult : LiveData<StoryResponse>
        get() = _addStoryResult
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    fun addStory(imageData: MultipartBody.Part, Description: RequestBody){
        storyRepository.addStory( imageData,Description).observeForever {
            _addStoryResult.value = it
        }
        storyRepository.isLoading.observeForever {
            _isLoading.value = it
        }
    }
}