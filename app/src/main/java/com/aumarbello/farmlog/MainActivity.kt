package com.aumarbello.farmlog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        if (authenticator.isUserLoggedIn()) {
            findNavController(R.id.container).apply {
                popBackStack()
                navigate(R.id.dashboardFragment)
            }
        }
    }
}
