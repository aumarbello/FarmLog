package com.aumarbello.farmlog.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.`is`

class RecyclerViewItemCountAssertion (private val count: Int): ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null)
            throw noViewFoundException

        if (view is RecyclerView) {
            val adapter = view.adapter ?: throw IllegalArgumentException("No Adapter attached to RecyclerView")
            assertThat(
                adapter.itemCount, `is`(count)
            )
        }
    }

    companion object {
        fun hasItems(count: Int) = RecyclerViewItemCountAssertion(count)
    }
}