package com.openclassrooms.realestatemanager.utils

import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar
import com.openclassrooms.realestatemanager.utils.Utils.formatInDollar
import com.openclassrooms.realestatemanager.utils.Utils.formatInEuro
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*


class UtilsTest {

    @Test
    fun `10 dollars convert to euro is equal to 8`() {
        val result = convertDollarToEuro(10)

        assertThat(result).isEqualTo(8)
    }

    @Test
    fun `10 dollars convert to euro is not equal to 9`() {
        val result = convertDollarToEuro(10)

        assertThat(result).isNotEqualTo(9)
    }

    @Test
    fun `10 euros convert to dollars is equal to 12`() {
        val result = convertEuroToDollar(10)

        assertThat(result).isEqualTo(12)
    }

    @Test
    fun `10 euros convert to dollars is not equal to 13`() {
        val result = convertEuroToDollar(10)

        assertThat(result).isNotEqualTo(13)
    }

    @Test
    fun `current date is formatted properly with this pattern dd-MM-yyyy`() {
        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val dateFormatted = dateFormat.format(currentDate.time)

        assertThat(dateFormatted).isEqualTo(Utils.getTodayDate())
    }

    @Test
    fun `current date is not formatted properly`() {
        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat()
        val dateFormatted = dateFormat.format(currentDate.time)

        assertThat(dateFormatted).isNotEqualTo(Utils.getTodayDate())
    }

    @Test
    fun `1000 is formatted correctly in dollars`() {
        val result = formatInDollar(1000, 2)
        assertThat(result).isEqualTo("$1,000.00")
    }

    @Test
    fun `1000 is formatted correctly in euros`() {
        val result = formatInEuro(1000, 2)
        assertThat(result).isEqualTo("1 000,00 €")
    }

}