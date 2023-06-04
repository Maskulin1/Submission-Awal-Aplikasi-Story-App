package com.reihan.finalawalstory

import android.app.Application
import com.reihan.finalawalstory.remote.module.networkModule
import com.reihan.finalawalstory.remote.module.repositoryModule
import com.reihan.finalawalstory.remote.module.sharePreferencesModule
import com.reihan.finalawalstory.remote.module.userManageModule
import com.reihan.finalawalstory.remote.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin{
            androidContext(this@MyApp)
            modules(
                networkModule, repositoryModule, viewModelModule, userManageModule, sharePreferencesModule
            )
        }
    }
}