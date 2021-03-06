package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.PropertyPhoto
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
import com.openclassrooms.realestatemanager.repositories.MainRepository
import kotlinx.coroutines.launch

class DetailsViewModel @ViewModelInject constructor(
        private val currentPropertyIdRepository: CurrentPropertyIdRepository,
        private val mainRepository: MainRepository,
) : ViewModel() {

    fun getViewStateLiveData() : LiveData<PropertyWithAllData> {
        return Transformations.switchMap(currentPropertyIdRepository.getCurrentPropertyIdLiveData()) { id ->
            mainRepository.getPropertyForId(id)
        }
    }

    fun insertPropertyPhoto(propertyPhoto: PropertyPhoto) = viewModelScope.launch {
        mainRepository.insertPropertyPhoto(propertyPhoto)
    }

    fun setCurrentPropertyId(id: Int) {
        currentPropertyIdRepository.setCurrentPropertyId(id)
    }

}