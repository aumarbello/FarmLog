package com.aumarbello.farmlog.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aumarbello.farmlog.R
import com.aumarbello.farmlog.databinding.FragmentDashboardBinding
import com.aumarbello.farmlog.di.FarmLogViewModelFactory
import com.aumarbello.farmlog.utils.EventObserver
import com.aumarbello.farmlog.utils.appComponent
import com.aumarbello.farmlog.utils.fadeView
import com.aumarbello.farmlog.utils.showSnackBar
import com.aumarbello.farmlog.viewmodels.DashboardViewModel
import javax.inject.Inject

class DashboardFragment: Fragment(R.layout.fragment_dashboard) {
    @Inject
    lateinit var factory: FarmLogViewModelFactory

    private lateinit var viewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding

    private val adapter = DashboardAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent?.inject(this)
        viewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDashboardBinding.bind(view)
        binding.dashboardItems.adapter = adapter

        setObservers()
        setListeners()
    }

    private fun setObservers() {
        viewModel.loader.observe(viewLifecycleOwner, Observer {
            binding.loader.fadeView(it)
        })

        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            showSnackBar(it)
        })

        viewModel.response.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun setListeners() {
        binding.addLogEntry.setOnClickListener {
            findNavController().navigate(R.id.newEntryFragment)
        }
    }
}