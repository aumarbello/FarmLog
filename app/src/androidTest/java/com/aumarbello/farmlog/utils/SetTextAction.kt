package com.aumarbello.farmlog.utils

import android.view.View
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

class SetTextAction (private val text: String): ViewAction {
    override fun getDescription(): String {
        return "Set text on a textView (or any of it's subclasses)"
    }

    override fun getConstraints(): Matcher<View> {
        return allOf(isDisplayed(), isAssignableFrom(TextView::class.java))
    }

    override fun perform(uiController: UiController?, view: View?) {
        if (view is TextView) {
            view.text = text
        }
    }

    companion object {
        fun setText(text: String) = SetTextAction(text)
    }
}