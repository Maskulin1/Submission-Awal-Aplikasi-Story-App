package com.reihan.finalawalstory.remote.module

import com.reihan.finalawalstory.model.DetailViewModel
import com.reihan.finalawalstory.model.LoginViewModel
import com.reihan.finalawalstory.model.RegisterViewModel
import com.reihan.finalawalstory.remote.preferences.SettingPreferences
import com.reihan.finalawalstory.remote.preferences.ViewModelPreferences
import com.reihan.finalawalstory.repository.LoginRepository
import com.reihan.finalawalstory.repository.RegisterRepository
import com.reihan.finalawalstory.repository.StoryRepository
import com.reihan.finalawalstory.ui.main.MainViewModel
import com.reihan.finalawalstory.ui.story.NewStoryViewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { LoginRepository(get()) }
    single { RegisterRepository(get()) }
    single { StoryRepository(get()) }
}

val viewModelModule = module {
    single { LoginViewModel(get()) }
    single { RegisterViewModel(get()) }
    single { NewStoryViewModel(get()) }
    single { ViewModelPreferences(get()) }
    single { MainViewModel(get()) }
    single { DetailViewModel(get()) }
}

val userManageModule = module {
    single { SettingPreferences(get()) }
}