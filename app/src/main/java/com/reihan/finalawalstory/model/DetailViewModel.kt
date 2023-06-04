package com.reihan.finalawalstory.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.finalawalstory.remote.data.Story
import com.reihan.finalawalstory.repository.StoryRepository

class DetailViewModel(private val detailRepository: StoryRepository) : ViewModel() {
    private val _detailResult = MutableLiveData<Story>()
    val detailResult : LiveData<Story>
        get() = _detailResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    fun fetchDetail(id:String){
        detailRepository.getDetailStory(id).observeForever {
            _detailResult.value = it
        }

        detailRepository.isLoading.observeForever {
            _isLoading.value = it
        }
    }

}