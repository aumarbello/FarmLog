package com.aumarbello.farmlog.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindsViewModelFactory(factory: FarmLogViewModelFactory): ViewModelProvider.Factory
}