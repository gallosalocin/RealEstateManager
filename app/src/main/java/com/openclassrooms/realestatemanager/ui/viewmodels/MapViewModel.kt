package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
import com.openclassrooms.realestatemanager.repositories.MainRepository

class MapViewModel @ViewModelInject constructor(
        private val currentPropertyIdRepository: CurrentPropertyIdRepository,
        private val mainRepository: MainRepository,
) : ViewModel() {

    fun getViewStateLiveData() : LiveData<PropertyWithAllData> {
        return Transformations.switchMap(currentPropertyIdRepository.getCurrentPropertyIdLiveData()) { id ->
            mainRepository.getPropertyForId(id)
        }
    }

    val getAllProperties: LiveData<List<PropertyWithAllData>> = mainRepository.observeAllProperties()

    fun setCurrentPropertyId(id: Int) {
        currentPropertyIdRepository.setCurrentPropertyId(id)
    }

}