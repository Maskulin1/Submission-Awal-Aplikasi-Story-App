package com.reihan.finalawalstory.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reihan.finalawalstory.remote.data.RegisterResponse
import com.reihan.finalawalstory.remote.service.RegisterService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository(private val registerService: RegisterService) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun registerUsers(name: String, email: String, password: String): LiveData<RegisterResponse> {
        val liveData = MutableLiveData<RegisterResponse>()
        _isLoading.value = true
        registerService.userRegister(name, email, password).enqueue(object :
            Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    liveData.value = response.body()
                } else {
                    Log.d("RegisterResponse", "onFailure:${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Register Repository", "onFailure : ${t.message}")
            }
        })
        return liveData
    }
}