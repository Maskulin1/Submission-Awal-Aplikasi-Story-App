package com.reihan.finalawalstory.remote.preferences

import androidx.lifecycle.ViewModel
import com.reihan.finalawalstory.model.UserModel

class ViewModelPreferences(private val settingPreferences: SettingPreferences) : ViewModel(){
    fun saveUserData(userModel: UserModel){
        settingPreferences.saveUser(userModel)
    }

    fun loadUserData():UserModel?{
        return settingPreferences.getUser()
    }
    fun clearUserData(){
        settingPreferences.clearUser()
    }
}