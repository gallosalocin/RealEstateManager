package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
        val mainRepository: MainRepository
) : ViewModel() {

    val getAllAgents = mainRepository.observeAllAgents()

    fun insertAgent(agent: Agent) = viewModelScope.launch {
        mainRepository.insertAgent(agent)
    }



}