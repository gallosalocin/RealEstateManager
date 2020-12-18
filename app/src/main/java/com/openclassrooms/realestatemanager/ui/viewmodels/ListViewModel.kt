package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
import com.openclassrooms.realestatemanager.repositories.MainRepository

class ListViewModel @ViewModelInject constructor(
        private val currentPropertyIdRepository: CurrentPropertyIdRepository,
        private val mainRepository: MainRepository,
) : ViewModel() {

    val getAllProperties: LiveData<List<PropertyWithAllData>> = mainRepository.observeAllProperties()

    fun setCurrentPropertyId(id: Int) {
        currentPropertyIdRepository.setCurrentPropertyId(id)
    }
}