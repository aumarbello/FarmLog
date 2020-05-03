package com.aumarbello.farmlog.ui

import androidx.fragment.app.Fragment
import com.aumarbello.farmlog.R
import com.aumarbello.farmlog.di.FarmLogViewModelFactory
import javax.inject.Inject

class EntryFragment: Fragment(R.layout.fragment_entry) {
    @Inject
    lateinit var factory: FarmLogViewModelFactory
}
