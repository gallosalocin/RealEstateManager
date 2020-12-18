package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.repositories.MainRepository

class LoginViewModel @ViewModelInject constructor(
        private val mainRepository: MainRepository,
) : ViewModel() {

    val getAllAgents: LiveData<List<Agent>> = mainRepository.observeAllAgents()

}