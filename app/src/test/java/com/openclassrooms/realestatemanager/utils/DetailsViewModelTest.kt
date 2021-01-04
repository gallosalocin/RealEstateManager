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

    private val propertyWithAllDataMutableLiveData = MutableLiveData<PropertyWithAllData>()
    private val currentPropertyIdMutableLiveData = MutableLiveData<Int>()

    private val mainRepository = Mockito.mock(MainRepository::class.java)
    private val currentPropertyIdRepository = Mockito.mock(CurrentPropertyIdRepository::class.java)
    private val utilsAsClass = Mockito.mock(UtilsAsClass::class.java)

    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setUp() {
        Mockito.doReturn(propertyWithAllDataMutableLiveData).`when`(mainRepository).getPropertyForId(Mockito.eq(EXPECTED_PROPERTY_ID))
        Mockito.doReturn(currentPropertyIdMutableLiveData).`when`(currentPropertyIdRepository).getCurrentPropertyIdLiveData()

        viewModel = DetailsViewModel(currentPropertyIdRepository, mainRepository, utilsAsClass)
    }

    @Test
    fun `nominal case - verify ViewState generation`() {
        // When
        viewModel.getViewStateLiveData().getOrAwaitValue {
            // Then
            Mockito.verify(mainRepository).getPropertyForId(Mockito.eq(EXPECTED_PROPERTY_ID))
            Mockito.verify(currentPropertyIdRepository).getCurrentPropertyIdLiveData()
            Mockito.verifyNoMoreInteractions(mainRepository, currentPropertyIdRepository)
        }
    }

    @Test
    fun `nominal case - base currency is dollar`() {
        // Given
        Mockito.doReturn("$150,000").`when`(utilsAsClass).formatInDollar(150_000, 0)

        propertyWithAllDataMutableLiveData.value = getDefaultPropertyWithAllData()

        // When
        val result = viewModel.getViewStateLiveData().getOrAwaitValue()

        // Then
        Truth.assertThat(result).isEqualTo(
            DetailsViewState(
                "$150,000"
            )
        )
    }

    @Test
    fun `nominal case - base currency is euro`() {
        // Given
        Mockito.doReturn("150 000€").`when`(utilsAsClass).formatInEuro(150_000, 0)

        propertyWithAllDataMutableLiveData.value = getDefaultPropertyWithAllData()

        // When
        val result = viewModel.getViewStateLiveData().getOrAwaitValue()

        // Then
        Truth.assertThat(result).isEqualTo(
            DetailsViewState(
                "150 000€"
            )
        )
    }


    @Test
    fun `edge case - base currency is dollar then user clicks on change currency button`() {
        // Given
        //Mockito.doReturn("$150,000").`when`(utilsAsClass).formatInDollar(150_000, 0)
        Mockito.doReturn("150 000€").`when`(utilsAsClass).formatInEuro(150_000, 0)

        propertyWithAllDataMutableLiveData.value = getDefaultPropertyWithAllData()

        // When
        viewModel.onCurrencyChangeButtonClicked()
        val result = viewModel.getViewStateLiveData().getOrAwaitValue()

        // Then
        Truth.assertThat(result).isEqualTo(
            DetailsViewState(
                "150 000€"
            )
        )
    }

    @Test
    fun `edge case - base currency is euro and price is less than 1'000 eurs`() {
        // Given
        Mockito.doReturn("200€").`when`(utilsAsClass).formatInEuro(200, 0)

        propertyWithAllDataMutableLiveData.value = getDefaultPropertyWithAllData(
            property = getDefaultProperty().copy(
                priceInDollars = 200
            )
        )

        // When
        val result = viewModel.getViewStateLiveData().getOrAwaitValue()

        // Then
        Truth.assertThat(result).isEqualTo(
            getDefaultDetailsViewState()
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
        "200€"
    )
}