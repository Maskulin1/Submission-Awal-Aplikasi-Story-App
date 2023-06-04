package com.reihan.finalawalstory.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reihan.finalawalstory.remote.data.LoginResponse
import com.reihan.finalawalstory.remote.data.LoginResult
import com.reihan.finalawalstory.remote.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val loginService: LoginService) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    fun userLogin(email:String, password:String): LiveData<LoginResult> {
        _isLoading.value = true
        val liveData = MutableLiveData<LoginResult>()
        loginService.userLogin(email,password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    Log.d(TAG, response.body().toString())
                    liveData.value = response.body()?.loginResult
                }else{
                    Log.d(TAG,"onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG,"onFailure : ${t.message}")
            }
        })
        return liveData
    }

    companion object {
        const val TAG = "Login Repositroy"
    }
}