package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyPhoto
import com.openclassrooms.realestatemanager.repositories.MainRepository
import com.openclassrooms.realestatemanager.ui.fragments.AddFragment
import com.openclassrooms.realestatemanager.ui.fragments.EditFragment
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MainViewModel @ViewModelInject constructor(
        val mainRepository: MainRepository
) : ViewModel() {

    val getAllAgents = mainRepository.observeAllAgents()

    fun insertAgent(agent: Agent) = viewModelScope.launch {
        mainRepository.insertAgent(agent)
    }


    val getAllProperties = mainRepository.observeAllProperties()

    val getLastPropertyAdded = mainRepository.observeLastPropertyAdded()

    fun insertProperty(property: Property) = viewModelScope.launch {
        mainRepository.insertProperty(property)
    }

    fun updateProperty(property: Property) = viewModelScope.launch {
        mainRepository.updateProperty(property)
    }


    val getAllPropertiesPhotos = mainRepository.observeAllPropertiesPhotos()

//    val getPropertyPhotos = mainRepository.observePropertyPhotos(EditFragment().propertyId)

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