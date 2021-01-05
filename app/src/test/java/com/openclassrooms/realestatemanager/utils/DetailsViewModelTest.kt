package com.openclassrooms.realestatemanager.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.repositories.CurrentPropertyIdRepository
import com.openclassrooms.realestatemanager.repositories.MainRepository
import com.openclassrooms.realestatemanager.ui.viewmodels.DetailsViewModel
import com.openclassrooms.realestatemanager.ui.viewmodels.DetailsViewState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailsViewModelTest {

    companion object {
        private const val EXPECTED_PROPERTY_ID = 42
    }

    // Instant update of the LiveDatas thanks to the InstantTaskExecutorRule (not really in this case because we switch
    // thread ourselves with *withContext(Dispatchers.Main)* but whatever this is usefull to know)
    // Note : A rule apply to every tests in this class
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val currentPropertyIdMutableLiveData = MutableLiveData<Int>()
    private val propertyWithAllDataMutableLiveData = MutableLiveData<PropertyWithAllData>()

    private val currentPropertyIdRepository = Mockito.mock(CurrentPropertyIdRepository::class.java)
    private val mainRepository = Mockito.mock(MainRepository::class.java)
    private val utilsAsClass = Mockito.mock(UtilsAsClass::class.java)

    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setUp() {
        propertyWithAllDataMutableLiveData.value = getDefaultPropertyWithAllData()
        currentPropertyIdMutableLiveData.value = EXPECTED_PROPERTY_ID

        Mockito.doReturn(currentPropertyIdMutableLiveData).`when`(currentPropertyIdRepository).getCurrentPropertyIdLiveData()
        Mockito.doReturn(propertyWithAllDataMutableLiveData).`when`(mainRepository).getPropertyForId(Mockito.eq(EXPECTED_PROPERTY_ID))
        Mockito.doReturn("$150,000").`when`(utilsAsClass).formatInDollar(150_000, 0)

        viewModel = DetailsViewModel(currentPropertyIdRepository, mainRepository, utilsAsClass)
    }

    @Test
    fun `nominal case - base currency is dollar`() {
        // When
        val result = viewModel.getViewStateLiveData().getOrAwaitValue()

        // Then
        Truth.assertThat(result).isEqualTo(getDefaultDetailsViewState())

        Mockito.verify(mainRepository).getPropertyForId(Mockito.eq(EXPECTED_PROPERTY_ID))
        Mockito.verify(currentPropertyIdRepository).getCurrentPropertyIdLiveData()
        Mockito.verifyNoMoreInteractions(mainRepository, currentPropertyIdRepository)
    }

    @Test
    fun `edge case - currency change to euro`() {
        // Given
        val euroPrice = "120 000€"
        Mockito.doReturn(euroPrice).`when`(utilsAsClass).formatInEuro(120_000, 0)
        Mockito.doReturn(120_000).`when`(utilsAsClass).convertDollarToEuro(150_000)

        // When
        viewModel.onCurrencyChangeButtonClicked()
        val result = viewModel.getViewStateLiveData().getOrAwaitValue()

        // Then
        Truth.assertThat(result).isEqualTo(
            getDefaultDetailsViewState().copy(
                price = euroPrice
            )
        )
    }

    // TODO This test doesn't really mean anything,
    //  it's just an example to see how easy it is to show in Kotlin what is tested with "copy()" function + named parameters
    @Test
    fun `edge case - currency change to euro and price is less than 1'000 euros`() {
        // Given
        Mockito.doReturn("150€").`when`(utilsAsClass).formatInEuro(150, 0)
        Mockito.doReturn(150).`when`(utilsAsClass).convertDollarToEuro(200)

        propertyWithAllDataMutableLiveData.value = getDefaultPropertyWithAllData(
            property = getDefaultProperty().copy(
                priceInDollars = 200
            )
        )

        // When
        viewModel.onCurrencyChangeButtonClicked()
        val result = viewModel.getViewStateLiveData().getOrAwaitValue()

        // Then
        Truth.assertThat(result).isEqualTo(
            getDefaultDetailsViewState().copy(
                price = "150€"
            )
        )
    }

    // IN
    private fun getDefaultPropertyWithAllData(property: Property = getDefaultProperty()) = PropertyWithAllData(
        property = property,
        listOf(),
        Agent()
    )

    private fun getDefaultProperty() = Property(
        type = "",
        priceInDollars = 150_000,
        street = "",
        postcode = "",
        city = "",
        country = "",
        availableDate = "",
        agentId = 0,
        coverPhoto = "",
        labelPhoto = "",
    )

    // OUT
    private fun getDefaultDetailsViewState() = DetailsViewState(
        "$150,000"
    )
}