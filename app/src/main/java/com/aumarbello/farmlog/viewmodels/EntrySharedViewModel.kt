package com.aumarbello.farmlog.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aumarbello.farmlog.OpenForTesting
import com.aumarbello.farmlog.models.FarmLocation
import javax.inject.Inject

@OpenForTesting
class EntrySharedViewModel @Inject constructor(): ViewModel() {
    private val _locations = MutableLiveData<List<FarmLocation>>()
    val locations: LiveData<List<FarmLocation>> = _locations

    private val _imagePath = MutableLiveData<String>()
    val imagePath: LiveData<String> = _imagePath

    private val _farmLocation = MutableLiveData<FarmLocation>()
    val farmLocation: LiveData<FarmLocation> = _farmLocation

    fun setImagePath(path: String) {
        _imagePath.value = path
    }

    fun addCoordinate(location: FarmLocation) {
        val currentLocations = (_locations.value ?: listOf()).toMutableList()
        currentLocations.add(location)

        _locations.value = currentLocations.toList()
    }

    fun removeCoordinate(location: FarmLocation) {
        val currentLocations = _locations.value ?: return
        _locations.value = currentLocations.toMutableList().apply {
            remove(location)
        }.toList()
    }

    fun setFarmLocation(location: FarmLocation) {
        _farmLocation.value = location
    }
}