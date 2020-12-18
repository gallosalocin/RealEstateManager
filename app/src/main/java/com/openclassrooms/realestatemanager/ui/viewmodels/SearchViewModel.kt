package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
import com.openclassrooms.realestatemanager.repositories.MainRepository

class SearchViewModel @ViewModelInject constructor(
        private val mainRepository: MainRepository,
        private val currentPropertyIdRepository: CurrentPropertyIdRepository,
        ) : ViewModel() {

    fun getAllFilteredProperties(
            agent: String, type: String, priceMin: String, priceMax: String, areaMin: String, areaMax: String, roomMin: String, roomMax: String,
            bedroomMin: String, bedroomMax: String, bathroomMin: String, bathroomMax: String, entryDateMin: String, entryDateMax: String,
            soldDateMin: String, soldDateMax: String, city: String, country: String
    ): LiveData<List<PropertyWithAllData>> {
        return mainRepository.observeAllFilteredProperties(
                agent, type, priceMin, priceMax, areaMin, areaMax, roomMin, roomMax, bedroomMin, bedroomMax, bathroomMin, bathroomMax,
                entryDateMin, entryDateMax, soldDateMin, soldDateMax, city, country
        )
    }

    fun setCurrentPropertyId(id: Int) {
        currentPropertyIdRepository.setCurrentPropertyId(id)
    }
}