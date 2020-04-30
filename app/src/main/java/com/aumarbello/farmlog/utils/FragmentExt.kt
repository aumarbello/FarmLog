package com.aumarbello.farmlog.utils

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.aumarbello.farmlog.FarmLogApplication
import com.aumarbello.farmlog.di.AppComponent
import com.aumarbello.farmlog.di.DaggerAppComponent
import com.google.android.material.snackbar.Snackbar

val Fragment.appComponent: AppComponent?
get() {
    val application = activity?.application
    return if (application is FarmLogApplication)
        DaggerAppComponent.builder().application(application).create()
    else
        null
}

fun Fragment.showSnackBar(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
}

fun Fragment.hideKeyboard() {
    view?.hideKeyboard()
}

fun View.hideKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(windowToken, 0)
}