package com.openclassrooms.realestatemanager.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*


class UtilsTest {

    @Test
    fun `10 euros convert to dollars is equal to 12`() {
        val result = Utils.convertEuroToDollar(10)

        assertThat(result).isEqualTo(12)
    }

    @Test
    fun `10 euros convert to dollars is not equal to 13`() {
        val result = Utils.convertEuroToDollar(10)

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
}