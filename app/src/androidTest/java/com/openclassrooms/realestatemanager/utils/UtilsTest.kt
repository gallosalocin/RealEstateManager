package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.widget.EditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.utils.Utils.isGPSEnabled
import com.openclassrooms.realestatemanager.utils.Utils.isInternetConnected
import com.openclassrooms.realestatemanager.utils.Utils.validateInputFieldIfIsGreaterThan
import com.openclassrooms.realestatemanager.utils.Utils.validateInputFieldIfNullOrEmpty
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*


@Suppress("DEPRECATION")
class UtilsIntegrationTest {

    private lateinit var context: Context

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun checkIfGpsIsEnabledOrNot() {
        val locationManager = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        val result = isGPSEnabled(context)

        assertThat(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).isEqualTo(result)
    }

    @Test
    fun checkIfInternetIsConnectedOrNot() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = Objects.requireNonNull(connectivityManager).activeNetworkInfo
        val result = isInternetConnected(context)

        assertThat(networkInfo != null && networkInfo.isConnected).isEqualTo(result)
    }

    @Test
    fun whenFieldIsNullReturnsFalse() {
        val field = mock(EditText::class.java)
        `when`(field.text.isNullOrEmpty()).thenReturn(null)

        val result = validateInputFieldIfNullOrEmpty(field, "error")
        assertThat(result).isFalse()
    }

    @Test
    fun whenFieldInputIsLessThan4ReturnsFalse() {
        val field = mock(EditText::class.java)
        `when`(field.length()).thenReturn(3)

        val result = validateInputFieldIfIsGreaterThan(field, "error", 4)
        assertThat(result).isFalse()
    }

    @Test
    fun whenFieldInputIsEqualAt4ReturnsTrue() {
        val field = mock(EditText::class.java)
        `when`(field.length()).thenReturn(4)

        val result = validateInputFieldIfIsGreaterThan(field, "error", 4)
        assertThat(result).isTrue()
    }

    @Test
    fun whenFieldInputIsGreaterThan4ReturnsTrue() {
        val field = mock(EditText::class.java)
        `when`(field.length()).thenReturn(5)

        val result = validateInputFieldIfIsGreaterThan(field, "error", 4)
        assertThat(result).isTrue()
    }

}