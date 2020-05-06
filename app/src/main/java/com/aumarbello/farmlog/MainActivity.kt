package com.aumarbello.farmlog

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.aumarbello.farmlog.data.UserAuthenticator
import com.aumarbello.farmlog.di.DaggerAppComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authenticator: UserAuthenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerAppComponent.builder().application(application).create().inject(this)
        val navController = findNavController(R.id.container)

        if (authenticator.isUserLoggedIn()) {
            navController.run {
                popBackStack()
                navigate(R.id.dashboardFragment)
            }
        }

        val fab = findViewById<View>(R.id.addLogEntry)
        fab.setOnClickListener {
            navController.navigate(R.id.new_entry)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            fab.isVisible = destination.id == R.id.dashboardFragment
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            findNavController(R.id.container).popBackStack()
        } else
            super.onOptionsItemSelected(item)
    }
}
