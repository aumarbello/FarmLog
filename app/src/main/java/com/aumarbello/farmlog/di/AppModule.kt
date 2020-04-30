package com.aumarbello.farmlog.di

import android.app.Application
import android.content.Context
import com.aumarbello.farmlog.data.InMemoryAuthenticator
import com.aumarbello.farmlog.data.UserAuthenticator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Provides
    @Singleton
    fun providesApplicationContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesUserAuthenticator(): UserAuthenticator {
        return InMemoryAuthenticator()
    }
}