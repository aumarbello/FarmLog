package com.aumarbello.farmlog.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aumarbello.farmlog.viewmodels.DashboardViewModel
import com.aumarbello.farmlog.viewmodels.EntrySharedViewModel
import com.aumarbello.farmlog.viewmodels.EntryViewModel
import com.aumarbello.farmlog.viewmodels.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindsViewModelFactory(factory: FarmLogViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindsLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindsDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EntryViewModel::class)
    abstract fun bindsEntryViewModel(viewModel: EntryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EntrySharedViewModel::class)
    abstract fun bindsEntryVSharedViewModel(viewModel: EntrySharedViewModel): ViewModel
}