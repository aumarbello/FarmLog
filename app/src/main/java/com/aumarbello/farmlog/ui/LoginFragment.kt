package com.aumarbello.farmlog.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.util.PatternsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aumarbello.farmlog.R
import com.aumarbello.farmlog.databinding.FragmentLoginBinding
import com.aumarbello.farmlog.di.FarmLogViewModelFactory
import com.aumarbello.farmlog.utils.*
import com.aumarbello.farmlog.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern
import javax.inject.Inject

class LoginFragment: Fragment(R.layout.fragment_login) {
    @Inject
    lateinit var factory: FarmLogViewModelFactory

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent?.inject(this)

        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLoginBinding.bind(view)
        updateToolbarTitle(R.string.label_login)

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
            findNavController().run {
                popBackStack()

                navigate(R.id.dashboardFragment)
            }
        })
    }

    private fun setListeners() {
        binding.login.setOnClickListener {
            if (validate()) {
                hideKeyboard()

                val email = binding.email.text.toString()
                val password = binding.password.text.toString()

                viewModel.login(email, password)
            }
        }
    }

    private fun validate(): Boolean {
        val email = binding.email.text.toString()
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Invalid email format"
            addTextWatch(binding.email, binding.emailLayout, PatternsCompat.EMAIL_ADDRESS)

            return false
        }

        val password = binding.password.text.toString()
        val passwordPattern = Pattern.compile(".{4,}")
        if (!passwordPattern.matcher(password).matches()) {
            binding.passwordLayout.error = "Password too short"
            addTextWatch(binding.password, binding.passwordLayout, passwordPattern)

            return false
        }

        return true
    }

    private fun addTextWatch(input: TextInputEditText, layout: TextInputLayout, pattern: Pattern) {
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (pattern.matcher(s.toString()).matches()) {
                    input.removeTextChangedListener(this)

                    layout.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}