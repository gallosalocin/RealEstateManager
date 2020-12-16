package com.openclassrooms.realestatemanager.repositories

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CurrentPropertyIdRepository {

    private val currentPropertyIdMutableLiveData = MutableLiveData<Int>()

    @MainThread
    fun setCurrentPropertyId(id: Int) {
        currentPropertyIdMutableLiveData.value = id
    }

    fun getCurrentPropertyIdLiveData() : LiveData<Int> = currentPropertyIdMutableLiveData

}