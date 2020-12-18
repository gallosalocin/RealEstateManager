package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.repositories.MainRepository
import kotlinx.coroutines.launch

class AddViewModel @ViewModelInject constructor(
        private val mainRepository: MainRepository,
) : ViewModel() {

    val getAllAgents: LiveData<List<Agent>> = mainRepository.observeAllAgents()

    fun insertProperty(property: Property) = viewModelScope.launch {
        mainRepository.insertProperty(property)
    }
}