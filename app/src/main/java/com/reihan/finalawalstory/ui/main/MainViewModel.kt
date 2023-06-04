package com.reihan.finalawalstory.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.finalawalstory.remote.data.ListStory
import com.reihan.finalawalstory.repository.StoryRepository

class MainViewModel(private val storyRepository: StoryRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    private val _fetchStory = MutableLiveData<List<ListStory>>()
    val fetchStory : LiveData<List<ListStory>>
        get() = _fetchStory

    fun fetchStory(){
        storyRepository.fetchStory().observeForever {
            _fetchStory.value = it
        }
        storyRepository.isLoading.observeForever {
            _isLoading.value = it
        }
    }
}