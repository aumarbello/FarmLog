package com.aumarbello.farmlog.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aumarbello.farmlog.getOrAwaitValue
import com.aumarbello.farmlog.models.FarmLocation
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EntrySharedViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: EntrySharedViewModel

    @Before
    fun setUp() {
        viewModel = EntrySharedViewModel()
    }

    @Test
    fun whenImageIsCapturedThenUpdateImagePath() {
        val path = "path/to/image"

        viewModel.setImagePath(path)

        assertThat(viewModel.imagePath.getOrAwaitValue(), `is`(path))
    }

    @Test
    fun whenCoordinateIsAddedThenUpdateFarmLocations() {
        val location = FarmLocation(6.412, 5.981)

        viewModel.addCoordinate(location)

        assertThat(viewModel.locations.getOrAwaitValue(), `is`(listOf(location)))
    }

    @Test
    fun whenCoordinateIsRemovedThenUpdateFarmLocations() {
        val locations = listOf (
            FarmLocation(6.412, 5.981),
            FarmLocation(6.302, 5.8710),
            FarmLocation(6.292, 5.761)
        )
        locations.forEach {
            viewModel.addCoordinate(it)
        }

        viewModel.removeCoordinate(FarmLocation(6.302, 5.8710))

        val updatedList = listOf (
            FarmLocation(6.412, 5.981),
            FarmLocation(6.292, 5.761)
        )
        assertThat(viewModel.locations.getOrAwaitValue(), `is`(updatedList))
    }

    @Test
    fun whenLocationIsSelectedThenUpdateFarmLocation() {
        val location = FarmLocation(6.302, 5.8710)

        viewModel.setFarmLocation(location)

        assertThat(viewModel.farmLocation.getOrAwaitValue(), `is`(location))
    }
}