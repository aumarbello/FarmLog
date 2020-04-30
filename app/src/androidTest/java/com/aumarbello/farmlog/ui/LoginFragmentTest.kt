package com.aumarbello.farmlog.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aumarbello.farmlog.CoroutineTestRule
import com.aumarbello.farmlog.R
import com.aumarbello.farmlog.mock
import com.aumarbello.farmlog.utils.Event
import com.aumarbello.farmlog.utils.IsErrorDisplayedMatcher.Companion.isErrorDisplayed
import com.aumarbello.farmlog.utils.TaskIdlingResourceRule
import com.aumarbello.farmlog.utils.ViewModelUtil
import com.aumarbello.farmlog.viewmodels.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class LoginFragmentTest {
    @get:Rule
    val executorRule = TaskIdlingResourceRule()

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    private val response = MutableLiveData<Boolean>()
    private val loader = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Event<String>>()
    private val navController = mock<NavController>()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = Mockito.mock(LoginViewModel::class.java)

        Mockito.`when`(viewModel.response).thenReturn(response)
        Mockito.`when`(viewModel.loader).thenReturn(loader)
        Mockito.`when`(viewModel.error).thenReturn(error)

        val scenario = launchFragmentInContainer(null, R.style.TestContainer) {
            LoginFragment().apply {
                factory = ViewModelUtil.createFor(viewModel)
            }
        }

        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
    }

    @Test
    fun whenCredentialsAreValidPassValuesToViewModel() {
        val email = "john@example.com"
        val password = "password"

        onView(withId(R.id.email))
            .perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.password))
            .perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.login)).perform(click())

        verify(viewModel).login(email, password)
    }

    @Test
    fun whenEmailAddressIsInvalidShowError() {
        val email = "john"
        val password = "password"

        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.login)).perform(click())

        onView(withId(R.id.emailLayout)).check(matches(isErrorDisplayed()))
    }

    @Test
    fun whenPasswordIsInvalidShowError() {
        val email = "john@example.com"
        val password = "pas"

        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.login)).perform(click())

        onView(withId(R.id.passwordLayout)).check(matches(isErrorDisplayed()))
    }

    @Test
    fun whenLoaderIsTrueDisplayProgressBar() {
        loader.postValue(true)

        onView(withId(R.id.loader))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun whenErrorOccursDisplayErrorView() {
        val event = Event("An error occurred")
        error.postValue(event)

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(event.peekContent())))
    }

    @Test
    fun whenLoginSuccessFulNavigateToDashboard() {
        response.postValue(true)

        verify(navController).navigate(R.id.to_dashboard)
    }
}