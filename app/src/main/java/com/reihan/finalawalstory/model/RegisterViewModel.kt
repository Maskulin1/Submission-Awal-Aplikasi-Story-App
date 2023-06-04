package com.reihan.finalawalstory.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.finalawalstory.remote.data.RegisterResponse
import com.reihan.finalawalstory.repository.RegisterRepository

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    private val _resultRegister = MutableLiveData<RegisterResponse>()
    val resultRegister: LiveData<RegisterResponse>
        get() = _resultRegister

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun userRegister(name: String, email: String, password: String) {
        registerRepository.registerUsers(name, email, password).observeForever {
            _resultRegister.value = it
        }
        registerRepository.isLoading.observeForever {
            _isLoading.value = it
        }
    }

}