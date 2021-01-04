package com.openclassrooms.realestatemanager.utils

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

class UtilsAsClass {

    /**
     *  Convert dollars to euros
     */
    fun convertDollarToEuro(dollar: Int): Int {
        return (dollar * 0.843).roundToInt()
    }

    /**
     *  Convert euros to dollars
     */
    fun convertEuroToDollar(euro: Int): Int {
        return (euro * 1.186).roundToInt()
    }

    /**
     *  Number format in dollar
     */
    fun formatInDollar(number: Number, maxDecimal: Int): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        numberFormat.maximumFractionDigits = maxDecimal
        return numberFormat.format(number)
    }

    /**
     *  Number format in euro
     */
    fun formatInEuro(number: Number, maxDecimal: Int): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        numberFormat.maximumFractionDigits = maxDecimal
        return numberFormat.format(number)
    }
}