package com.aumarbello.farmlog.utils

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class IsErrorDisplayedMatcher: TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("has visible error message")
    }

    override fun matchesSafely(item: View?): Boolean {
        return item is TextInputLayout && item.isErrorEnabled  && item.error != null
    }

    companion object {
        fun isErrorDisplayed() = IsErrorDisplayedMatcher()
    }
}