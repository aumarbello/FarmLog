package com.aumarbello.farmlog.utils

import android.view.View
import android.widget.ImageView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

class SetImageViewTagAction (private val tag: String): ViewAction {
    override fun getDescription(): String {
        return "Sets tag $tag on an image view"
    }

    override fun getConstraints(): Matcher<View> {
        return allOf(isDisplayed(), isAssignableFrom(ImageView::class.java))
    }

    override fun perform(uiController: UiController?, view: View?) {
        if (view is ImageView) {
            view.tag = tag
        }
    }

    companion object {
        fun setTag(tag: String) = SetImageViewTagAction(tag)
    }
}