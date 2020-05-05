package com.aumarbello.farmlog.di

import android.app.Application
import android.content.Context
import com.aumarbello.farmlog.data.InMemoryAuthenticator
import com.aumarbello.farmlog.data.UserAuthenticator
import com.aumarbello.farmlog.data.db.FarmLogDAO
import com.aumarbello.farmlog.data.db.FarmLogDatabase
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
    fun providesUserAuthenticator(context: Context): UserAuthenticator {
        return InMemoryAuthenticator(context)
    }

    @Provides
    @Singleton
    fun providesFarmLogDAO(context: Context): FarmLogDAO {
        return FarmLogDatabase.getInstance(context).logsDAO()
    }
}