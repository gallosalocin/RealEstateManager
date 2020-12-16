package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.database.Property
import com.openclassrooms.realestatemanager.models.database.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
import com.openclassrooms.realestatemanager.repositories.MainRepository


class DetailsViewModel @ViewModelInject constructor(
    private val currentPropertyIdRepository: CurrentPropertyIdRepository,
    private val mainRepository: MainRepository,
) : ViewModel() {

    fun getViewStateLiveData() : LiveData<PropertyWithAllData> {
        return Transformations.switchMap(currentPropertyIdRepository.getCurrentPropertyIdLiveData()) { id ->
            mainRepository.getPropertyForId(id)
        }
    }

}