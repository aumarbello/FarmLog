package com.aumarbello.farmlog.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aumarbello.farmlog.CoroutineTestRule
import com.aumarbello.farmlog.R
import com.aumarbello.farmlog.mock
import com.aumarbello.farmlog.models.FarmLocation
import com.aumarbello.farmlog.models.FarmLogEntity
import com.aumarbello.farmlog.utils.Event
import com.aumarbello.farmlog.utils.IsErrorDisplayedMatcher.Companion.isErrorDisplayed
import com.aumarbello.farmlog.utils.TaskIdlingResourceRule
import com.aumarbello.farmlog.utils.ViewModelUtil
import com.aumarbello.farmlog.viewmodels.EntrySharedViewModel
import com.aumarbello.farmlog.viewmodels.EntryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class EntryFragmentTest {
    @get:Rule
    val executorRule = TaskIdlingResourceRule()

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    @get:Rule
    val intentsTestRule = IntentsTestRule(FragmentScenario.EmptyFragmentActivity::class.java)

    private val response = MutableLiveData<Unit>()
    private val loader = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Event<String>>()
    private val locations = MutableLiveData<List<FarmLocation>>()
    private val farmLocation = MutableLiveData<FarmLocation>()
    private val imagePath = MutableLiveData<String>()
    private lateinit var navController: NavController

    private lateinit var viewModel: EntryViewModel
    private lateinit var sharedViewModel: EntrySharedViewModel
    private lateinit var scenario: FragmentScenario<EntryFragment>

    @Before
    fun setUp() {
        viewModel = Mockito.mock(EntryViewModel::class.java)
        sharedViewModel = Mockito.mock(EntrySharedViewModel::class.java)

        `when`(viewModel.loader).thenReturn(loader)
        `when`(viewModel.response).thenReturn(response)
        `when`(viewModel.error).thenReturn(error)

        `when`(sharedViewModel.imagePath).thenReturn(imagePath)
        `when`(sharedViewModel.locations).thenReturn(locations)
        `when`(sharedViewModel.farmLocation).thenReturn(farmLocation)

        navController = mock()

        scenario = launchFragmentInContainer(themeResId = R.style.TestContainer) {
            EntryFragment().apply {
                factory = ViewModelUtil.createFor(viewModel, sharedViewModel)
            }
        }

        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
    }

    @Test
    fun whenImageIsNotSelectedThenShowError() {
        onView(withId(R.id.save)).perform(scrollTo(), click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText((R.string.error_image_not_selected))))
    }

    @Test
    fun whenFarmersNameNotValidThenDisplayError() {
        imagePath.postValue("pictures/imageTaken.png")

        onView(withId(R.id.fullName)).perform(typeText("J P"), closeSoftKeyboard())
        onView(withId(R.id.save)).perform(scrollTo(), click())

        onView(withId(R.id.nameLayout)).check(matches(isErrorDisplayed((R.string.error_full_name))))
    }

    @Test
    fun whenPhoneNumberIsNotValidThenDisplayError() {
        imagePath.postValue("pictures/imageTaken.png")
        onView(withId(R.id.fullName)).perform(typeText("Bayo Musa"), closeSoftKeyboard())

        onView(withId(R.id.phoneNumber)).perform(typeText("08141"), closeSoftKeyboard())
        onView(withId(R.id.save)).perform(scrollTo(), click())

        onView(withId(R.id.phoneNumberLayout)).check(matches(isErrorDisplayed(R.string.error_phone_number)))
    }

    @Test
    fun whenAgeProvideIsNotValidThenDisplayError() {
        imagePath.postValue("pictures/imageTaken.png")
        onView(withId(R.id.fullName)).perform(typeText("Bayo Musa"), closeSoftKeyboard())
        onView(withId(R.id.phoneNumber)).perform(typeText("08012312312"), closeSoftKeyboard())

        onView(withId(R.id.age)).perform(scrollTo(), typeText("12"), closeSoftKeyboard())
        onView(withId(R.id.save)).perform(scrollTo(), click())

        onView(withId(R.id.ageLayout)).check(matches(isErrorDisplayed(R.string.error_age)))
    }

    @Test
    fun whenGenderNotSelectedThenDisplayError() {
        imagePath.postValue("pictures/imageTaken.png")
        onView(withId(R.id.fullName)).perform(typeText("Bayo Musa"), closeSoftKeyboard())
        onView(withId(R.id.phoneNumber)).perform(typeText("08012312312"), closeSoftKeyboard())
        onView(withId(R.id.age)).perform(scrollTo(), typeText("27"), closeSoftKeyboard())

        onView(withId(R.id.save)).perform(scrollTo(), click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText((R.string.error_gender))))
    }

    @Test
    fun whenFarmNameNotValidThenDisplayError() {
        imagePath.postValue("pictures/imageTaken.png")
        onView(withId(R.id.fullName)).perform(typeText("Bayo Musa"), closeSoftKeyboard())
        onView(withId(R.id.phoneNumber)).perform(typeText("08012312312"), closeSoftKeyboard())
        onView(withId(R.id.age)).perform(scrollTo(), typeText("27"), closeSoftKeyboard())
        onView(withId(R.id.female)).perform(scrollTo(), click())

        onView(withId(R.id.save)).perform(scrollTo(), click())

        onView(withId(R.id.farmNameLayout)).check(matches(isErrorDisplayed(R.string.error_farm_name)))
    }

    @Test
    fun whenFarmLocationNotSelectedThenDisplayError() {
        imagePath.postValue("pictures/imageTaken.png")
        onView(withId(R.id.fullName)).perform(typeText("Bayo Musa"), closeSoftKeyboard())
        onView(withId(R.id.phoneNumber)).perform(typeText("08012312312"), closeSoftKeyboard())
        onView(withId(R.id.age)).perform(scrollTo(), typeText("27"), closeSoftKeyboard())
        onView(withId(R.id.female)).perform(scrollTo(), click())
        onView(withId(R.id.farmName)).perform(
            scrollTo(),
            typeText("SK Farms LTD"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.save)).perform(scrollTo(), click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText((R.string.error_farm_location))))
    }

    @Test
    fun whenCoordinatesProvidedDontFormPolygonDisplayError() {
        imagePath.postValue("pictures/imageTaken.png")
        onView(withId(R.id.fullName)).perform(typeText("Bayo Musa"), closeSoftKeyboard())
        onView(withId(R.id.phoneNumber)).perform(typeText("08012312312"), closeSoftKeyboard())
        onView(withId(R.id.age)).perform(scrollTo(), typeText("27"), closeSoftKeyboard())
        onView(withId(R.id.female)).perform(scrollTo(), click())
        onView(withId(R.id.farmName)).perform(
            scrollTo(),
            typeText("SK Farms LTD"),
            closeSoftKeyboard()
        )

        farmLocation.postValue(FarmLocation(6.145, 2.509))

        onView(withId(R.id.save)).perform(scrollTo(), click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText((R.string.error_coordinates))))
    }

    @Test
    fun whenInputsAreAllValidThenCallViewModel() {
        imagePath.postValue("pictures/imageTaken.png")
        onView(withId(R.id.fullName)).perform(typeText("Bayo Musa"), closeSoftKeyboard())
        onView(withId(R.id.phoneNumber)).perform(typeText("08012312312"), closeSoftKeyboard())
        onView(withId(R.id.age)).perform(scrollTo(), typeText("27"), closeSoftKeyboard())
        onView(withId(R.id.female)).perform(scrollTo(), click())
        onView(withId(R.id.farmName)).perform(
            scrollTo(),
            typeText("SK Farms LTD"),
            closeSoftKeyboard()
        )
        farmLocation.postValue(FarmLocation(6.145, 2.509))

        locations.postValue(
            listOf(
                FarmLocation(6.412, 5.981),
                FarmLocation(6.302, 5.8710),
                FarmLocation(6.292, 5.761),
                FarmLocation(6.182, 5.661)
            )
        )

        onView(withId(R.id.save)).perform(scrollTo(), click())

        val entry = FarmLogEntity(
            "pictures/imageTaken.png",
            "Bayo Musa",
            27,
            "F",
            "SK Farms LTD",
            FarmLocation(6.145, 2.509),
            listOf(
                FarmLocation(6.412, 5.981),
                FarmLocation(6.302, 5.8710),
                FarmLocation(6.292, 5.761),
                FarmLocation(6.182, 5.661)
            )
        )

        verify(viewModel).addFarm(entry)
    }

    @Test
    fun whenProfileImageClickedOpenImageFragment() {
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                Intent()
            )
        )

        onView(withId(R.id.profileImage)).perform(click())

        intended(
            allOf(
                hasAction(MediaStore.ACTION_IMAGE_CAPTURE),
                hasExtraWithKey(MediaStore.EXTRA_OUTPUT)
            )
        )
    }

    @Test
    fun whenLoadingThenDisplayProgressBar() {
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
    fun whenEntrySavedSuccessfullyThenNavigateBack() {
        response.postValue(Unit)

        verify(navController).popBackStack()
    }

    @Test
    fun whenAddCoordinatesIsClickedThenDisplayLocationOptions() {
        onView(withId(R.id.addCoordinate)).perform(scrollTo(), click())

        onView(withText(R.string.label_dialog_title)).check(matches(isDisplayed()))
        onView(withText(R.string.action_current_location)).check(matches(isDisplayed()))
        onView(withText(R.string.action_pick_on_map)).check(matches(isDisplayed()))
    }

    @Test
    fun whenFarmLayoutIsClickedTheDisplayLocationOptions() {
        onView(withId(R.id.farmLocationLayout)).perform(scrollTo(), click())

        onView(withText(R.string.label_dialog_title)).check(matches(isDisplayed()))
        onView(withText(R.string.action_current_location)).check(matches(isDisplayed()))
        onView(withText(R.string.action_pick_on_map)).check(matches(isDisplayed()))
    }

    @Test
    fun whenCoordinatesLessThanThreeThenHideViewOnMap() {
        //Do nothing, test against initial state

        onView(withId(R.id.viewOnMap)).check(matches(not(isDisplayed())))
    }

    @Test
    fun whenCoordinatesAreThreeOrMoreThenShowViewOnMap() {
        locations.postValue(
            listOf(
                FarmLocation(6.412, 5.981),
                FarmLocation(6.302, 5.8710),
                FarmLocation(6.292, 5.761),
                FarmLocation(6.182, 5.661)
            )
        )

        onView(withId(R.id.viewOnMap)).perform(scrollTo())
        onView(withId(R.id.viewOnMap)).check(matches(isDisplayed()))
    }
}