package com.openclassrooms.realestatemanager.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.models.PropertyPhoto
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
import com.openclassrooms.realestatemanager.repositories.MainRepository
import com.openclassrooms.realestatemanager.utils.Utils.formatInDollar
import com.openclassrooms.realestatemanager.utils.Utils.formatInEuro
import com.openclassrooms.realestatemanager.utils.UtilsAsClass
import kotlinx.coroutines.launch

class DetailsViewModel @ViewModelInject constructor(
    private val currentPropertyIdRepository: CurrentPropertyIdRepository,
    private val mainRepository: MainRepository,
    private val utilsAsClass: UtilsAsClass,
) : ViewModel() {

    private val currencyTypeMutableLiveData = MutableLiveData(CurrencyType.DOLLAR)

    private val viewStateMediatorLiveData = MediatorLiveData<DetailsViewState>()

    init {
        val propertyLiveData = Transformations.switchMap(currentPropertyIdRepository.getCurrentPropertyIdLiveData()) { id ->
            mainRepository.getPropertyForId(id)
        }

        viewStateMediatorLiveData.addSource(propertyLiveData) {
            combine(it, currencyTypeMutableLiveData.value!!) // CurrencyType is always initialized
        }

        viewStateMediatorLiveData.addSource(currencyTypeMutableLiveData) {
            combine(propertyLiveData.value, it)
        }
    }

    private fun combine(propertyWithAllData: PropertyWithAllData?, currencyType: CurrencyType) {
        if (propertyWithAllData == null) {
            return
        }

        viewStateMediatorLiveData.value = DetailsViewState(
            when (currencyType) {
                CurrencyType.EURO -> utilsAsClass.formatInEuro(utilsAsClass.convertDollarToEuro(propertyWithAllData.property.priceInDollars), 0)
                CurrencyType.DOLLAR -> utilsAsClass.formatInDollar(propertyWithAllData.property.priceInDollars, 0)
            }
        )
    }

    fun getViewStateLiveData() : LiveData<DetailsViewState> = viewStateMediatorLiveData

    fun insertPropertyPhoto(propertyPhoto: PropertyPhoto) = viewModelScope.launch {
        mainRepository.insertPropertyPhoto(propertyPhoto)
    }

    fun setCurrentPropertyId(id: Int) {
        currentPropertyIdRepository.setCurrentPropertyId(id)
    }

    fun onCurrencyChangeButtonClicked() {
        currencyTypeMutableLiveData.value?.let {
            currencyTypeMutableLiveData.value = when (it) {
                CurrencyType.EURO -> CurrencyType.DOLLAR
                CurrencyType.DOLLAR -> CurrencyType.EURO
            }
        }
    }
}