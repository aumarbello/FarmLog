package com.aumarbello.farmlog.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aumarbello.farmlog.CoroutineTestRule
import com.aumarbello.farmlog.mock
import com.aumarbello.farmlog.utils.Event
import com.aumarbello.farmlog.utils.TaskIdlingResourceRule
import com.aumarbello.farmlog.utils.ViewModelUtil
import com.aumarbello.farmlog.viewmodels.EntryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class EntryFragmentTest {
    /**
     * 1. Snackbar error displayed when img resource is placeholder res
     * 2. Input layout error visible when invalid name format entered
     * 3. Input layout error visible when invalid phone number entered
     * 4. Input layout error when age entered is wrong
     * 5. Snackbar error when gender is not selected
     * 6. Input layout error when farm name is not valid
     * 7. Snackbar error when farm location textView's text == Farm location
     * 8. Snackbar error when coordinates provided are less that three(3)
     * 9. Verify view on map on visible only when recyclerView has 3 or more items
     * 10. Verify clicking on addCoordinate opens dialog with buttons 'current location' and pick on map
     * 11. Verify clicking on image opens ImageFragment
     * 12. Verify viewModel called when all inputs are valid
     * 13. Verify when request is ongoing loader is displayed
     * 14. Verify when error occurs Snackbar error is displayed
     */
    @get:Rule
    val executorRule = TaskIdlingResourceRule()

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    private val response = MutableLiveData<Unit>()
    private val loader = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Event<String>>()
    private val navController = mock<NavController>()

    private lateinit var viewModel: EntryViewModel

    @Before
    fun setUp() {
        viewModel = Mockito.mock(EntryViewModel::class.java)

        `when`(viewModel.loader).thenReturn(loader)
        `when`(viewModel.response).thenReturn(response)
        `when`(viewModel.error).thenReturn(error)

        val scenario = launchFragmentInContainer {
            EntryFragment().apply {
                factory = ViewModelUtil.createFor(viewModel)
            }
        }

        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
    }


}