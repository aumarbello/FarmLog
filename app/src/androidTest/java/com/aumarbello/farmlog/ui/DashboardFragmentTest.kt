package com.aumarbello.farmlog.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aumarbello.farmlog.CoroutineTestRule
import com.aumarbello.farmlog.R
import com.aumarbello.farmlog.TestObjects
import com.aumarbello.farmlog.mock
import com.aumarbello.farmlog.models.DashboardItem
import com.aumarbello.farmlog.models.DashboardItem.*
import com.aumarbello.farmlog.utils.Event
import com.aumarbello.farmlog.utils.RecyclerViewMatcher.Companion.withList
import com.aumarbello.farmlog.utils.TaskIdlingResourceRule
import com.aumarbello.farmlog.utils.ViewModelUtil
import com.aumarbello.farmlog.viewmodels.DashboardViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DashboardFragmentTest {
    @get:Rule
    val executorRule = TaskIdlingResourceRule()

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    private val response = MutableLiveData<List<DashboardItem>>()
    private val loader = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Event<String>>()
    private val navController = mock<NavController>()

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        viewModel = Mockito.mock(DashboardViewModel::class.java)

        `when`(viewModel.error).thenReturn(error)
        `when`(viewModel.loader).thenReturn(loader)
        `when`(viewModel.response).thenReturn(response)

        val scenario = launchFragmentInContainer (themeResId = R.style.TestContainer) {
            DashboardFragment().apply {
                factory = ViewModelUtil.createFor(viewModel)
            }
        }

        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
    }

    @Test
    fun whenResponseIsLoadedDisplayItemsGrid() {
        response.postValue(TestObjects.dashboardItems)

        onView(withId(R.id.dashboardItems)).check(matches(isDisplayed()))

        val countItem = TestObjects.dashboardItems[0] as CountItem
        onView(withList(R.id.dashboardItems).atPosition(0)).apply {
            check(matches(hasDescendant(withText(countItem.title))))
            check(matches(hasDescendant(withText(countItem.count.toString()))))
        }

        val weeksItem = TestObjects.dashboardItems[1] as BarChartItem
        onView(withList(R.id.dashboardItems).atPosition(1)).apply {
            check(matches(hasDescendant(withText(weeksItem.title))))
            check(matches(hasDescendant(withId(R.id.barChart))))
        }

        val ageItem = TestObjects.dashboardItems[2] as PieChartItem
        onView(withList(R.id.dashboardItems).atPosition(2)).apply {
            check(matches(hasDescendant(withText(ageItem.title))))
            check(matches(hasDescendant(withId(R.id.pieChart))))
        }

        val genderItem = TestObjects.dashboardItems[3] as PieChartItem
        onView(withList(R.id.dashboardItems).atPosition(3)).apply {
            check(matches(hasDescendant(withText(genderItem.title))))
            check(matches(hasDescendant(withId(R.id.pieChart))))
        }
    }

    @Test
    fun whenLoaderIsTrueDisplayProgressBar() {
        loader.postValue(true)

        onView(withId(R.id.loader)).check(matches(isDisplayed()))
    }

    @Test
    fun whenErrorOccursDisplayErrorView() {
        val event = Event("An error occurred")
        error.postValue(event)

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(event.peekContent())))
    }

    @Test
    fun whenFABIsClickedNavigateToAddEntry() {
        onView(withId(R.id.addLogEntry)).perform(click())

        verify(navController).navigate(R.id.newEntryFragment)
    }
}