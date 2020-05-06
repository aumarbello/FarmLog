package com.aumarbello.farmlog.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aumarbello.farmlog.R
import com.aumarbello.farmlog.databinding.FragmentDashboardBinding
import com.aumarbello.farmlog.di.FarmLogViewModelFactory
import com.aumarbello.farmlog.utils.*
import com.aumarbello.farmlog.viewmodels.DashboardViewModel
import com.google.android.material.snackbar.Snackbar
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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.logOut) {
            Snackbar.make(requireView(), R.string.label_logOut_prompt, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_logOut) {
                    viewModel.logOut()

                    findNavController().run {
                        popBackStack()
                        navigate(R.id.loginFragment)
                    }
                }
                .show()

            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDashboardBinding.bind(view)
        updateToolbarTitle(R.string.label_dashboard)

        binding.dashboardItems.also {
            it.adapter = adapter
            it.addItemDecoration(GridSpacingDecorator())
        }

        viewModel.loadDashboard()

        setObservers()
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

            binding.tvEmpty.fadeView(it.isEmpty())
        })
    }
}