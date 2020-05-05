package com.aumarbello.farmlog.di

import android.app.Application
import com.aumarbello.farmlog.MainActivity
import com.aumarbello.farmlog.ui.DashboardFragment
import com.aumarbello.farmlog.ui.EntryFragment
import com.aumarbello.farmlog.ui.LoginFragment
import com.aumarbello.farmlog.ui.MapFragment
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
    fun inject(fragment: DashboardFragment)
    fun inject(fragment: EntryFragment)
    fun inject(fragment: MapFragment)
    fun inject(activity: MainActivity)
}