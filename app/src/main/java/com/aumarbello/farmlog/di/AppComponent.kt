package com.aumarbello.farmlog.di

import android.app.Application
import com.aumarbello.farmlog.ui.LoginFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun create(): AppComponent
    }

    fun inject(fragment: LoginFragment)
}