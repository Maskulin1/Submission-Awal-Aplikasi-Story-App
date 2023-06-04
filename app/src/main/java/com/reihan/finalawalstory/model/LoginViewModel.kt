package com.reihan.finalawalstory.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.finalawalstory.remote.data.LoginResult
import com.reihan.finalawalstory.repository.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel(){
    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult : LiveData<LoginResult?>
        get() = _loginResult

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    fun userLogin(email:String, password:String){
        loginRepository.userLogin(email, password).observeForever{
            _loginResult.value = it
            Log.d("LoginViewModel", it.toString())
        }
        loginRepository.isLoading.observeForever {
            _showLoading.value = it
        }
    }
}