package com.openclassrooms.realestatemanager.ui.viewmodels

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.PropertyPhoto
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
import com.openclassrooms.realestatemanager.repositories.MainRepository
import com.openclassrooms.realestatemanager.ui.state.DetailUiState
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.launch

class DetailsViewModel @ViewModelInject constructor(
    private val currentPropertyIdRepository: CurrentPropertyIdRepository,
    private val mainRepository: MainRepository,
    // TODO This injected context is OK because it's an application Context
    private val context : Context
) : ViewModel() {

    private val uiStateMediatorLiveData = MediatorLiveData<DetailUiState>()

    private val isDollarLiveData = MutableLiveData(true)

    init {
        val propertyLiveData = Transformations.switchMap(currentPropertyIdRepository.getCurrentPropertyIdLiveData()) { id ->
            mainRepository.getPropertyForId(id)
        }

        uiStateMediatorLiveData.addSource(propertyLiveData) {
            combine(it, isDollarLiveData.value)
        }

        uiStateMediatorLiveData.addSource(isDollarLiveData) {
            combine(propertyLiveData.value, it)
        }
    }

    private fun combine(propertyWrapper: PropertyWithAllData?, isDollar: Boolean?) {
        if (propertyWrapper == null) {
            return
        }

        uiStateMediatorLiveData.value = DetailUiState(
            id = propertyWrapper.property.id,
            price = displayConvertedAndFormattedPrice(propertyWrapper.property.priceInDollars, isDollar == true),
            currencyIcon = if (isDollar == true) {
                R.drawable.ic_dollar
            } else {
                R.drawable.ic_euro
            },
            // TODO MAPPING...
        )
    }

    fun getViewStateLiveData(): LiveData<DetailUiState> = uiStateMediatorLiveData

    fun insertPropertyPhoto(propertyPhoto: PropertyPhoto) = viewModelScope.launch {
        mainRepository.insertPropertyPhoto(propertyPhoto)
    }

    fun setCurrentPropertyId() {
        uiStateMediatorLiveData.value?.id?.let {
            currentPropertyIdRepository.setCurrentPropertyId(it)
        }
    }

    fun onCurrencyClicked() {
        isDollarLiveData.value?.let {
            isDollarLiveData.value = !it
        }
    }

    // Display price in Dollars or Euro
    private fun displayConvertedAndFormattedPrice(currentPropertyPrice: Int, isDollar: Boolean): String = if (isDollar) {
        Utils.formatInEuro(Utils.convertDollarToEuro(currentPropertyPrice), 0)
    } else {
        Utils.formatInDollar(currentPropertyPrice, 0)
    }
}