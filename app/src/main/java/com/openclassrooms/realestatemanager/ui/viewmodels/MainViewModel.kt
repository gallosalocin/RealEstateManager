package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.database.Agent
import com.openclassrooms.realestatemanager.models.database.Property
import com.openclassrooms.realestatemanager.models.database.PropertyPhoto
import com.openclassrooms.realestatemanager.models.database.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
import com.openclassrooms.realestatemanager.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
        private val mainRepository: MainRepository,
        private val currentPropertyIdRepository : CurrentPropertyIdRepository
) : ViewModel() {

    val getAllAgents: LiveData<List<Agent>> = mainRepository.observeAllAgents()

    val getAllProperties: LiveData<List<PropertyWithAllData>> = mainRepository.observeAllProperties()

    val getAllPropertiesPhotos: LiveData<List<PropertyPhoto>> = mainRepository.observeAllPropertiesPhotos()


    // Agent

    fun insertAgent(agent: Agent) = viewModelScope.launch {
        mainRepository.insertAgent(agent)
    }

    // Property

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

    fun insertProperty(property: Property) = viewModelScope.launch {
        mainRepository.insertProperty(property)
    }

    fun updateProperty(property: Property) = viewModelScope.launch {
        mainRepository.updateProperty(property)
    }

    // Property Photo

    fun getPropertyPhotos(propertyId: Int) = viewModelScope.launch {
        mainRepository.observePropertyPhotos(propertyId)
    }

    fun insertPropertyPhoto(propertyPhoto: PropertyPhoto) = viewModelScope.launch {
        mainRepository.insertPropertyPhoto(propertyPhoto)
    }

    fun updatePropertyPhoto(propertyPhoto: PropertyPhoto) = viewModelScope.launch {
        mainRepository.updatePropertyPhoto(propertyPhoto)
    }

    fun deletePropertyPhoto(propertyPhoto: PropertyPhoto) = viewModelScope.launch {
        mainRepository.deletePropertyPhoto(propertyPhoto)
    }

    fun setCurrentPropertyId(id: Int) {
        currentPropertyIdRepository.setCurrentPropertyId(id)
    }
}