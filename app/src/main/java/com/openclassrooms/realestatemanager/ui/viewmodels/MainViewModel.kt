package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyPhoto
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.MainRepository
import com.openclassrooms.realestatemanager.ui.fragments.AddFragment
import com.openclassrooms.realestatemanager.ui.fragments.EditFragment
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MainViewModel @ViewModelInject constructor(
        val mainRepository: MainRepository
) : ViewModel() {


    // Agent

    val getAllAgents = mainRepository.observeAllAgents()

    fun insertAgent(agent: Agent) = viewModelScope.launch {
        mainRepository.insertAgent(agent)
    }

    // Property

    fun getAllFilteredProperties(
            agent: String, type: String,
            priceMin: String, priceMax: String,
            areaMin: String, areaMax: String,
            roomMin: String, roomMax: String,
            bedroomMin: String, bedroomMax: String,
            bathroomMin: String, bathroomMax: String,
            entryDateMin: String, entryDateMax: String,
            soldDateMin: String, soldDateMax: String,
            city: String, country: String
    ) : LiveData<List<PropertyWithAllData>>  {
          return mainRepository.observeAllFilteredProperties(
                  agent, type, priceMin, priceMax, areaMin, areaMax, roomMin, roomMax, bedroomMin, bedroomMax, bathroomMin, bathroomMax,
                  entryDateMin, entryDateMax, soldDateMin, soldDateMax, city, country
                  )
    }

    val getAllProperties = mainRepository.observeAllProperties()

    val getLastPropertyAdded = mainRepository.observeLastPropertyAdded()

    fun insertProperty(property: Property) = viewModelScope.launch {
        mainRepository.insertProperty(property)
    }

    fun updateProperty(property: Property) = viewModelScope.launch {
        mainRepository.updateProperty(property)
    }

    // Property Photo

    val getAllPropertiesPhotos = mainRepository.observeAllPropertiesPhotos()

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
}