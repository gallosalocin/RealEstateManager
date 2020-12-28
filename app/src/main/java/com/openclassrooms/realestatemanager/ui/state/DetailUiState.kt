package com.openclassrooms.realestatemanager.ui.state

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class DetailUiState(
    val id: Int,
    val type: String,
    val price: String,
    @DrawableRes
    val currencyIcon : Int,
    val areaInMeters: String,
    val nbrRoom: String,
    val nbrBedroom: String,
    val nbrBathroom: String,
    val description: String,
    val street: String,
    val postcode: String,
    val city: String,
    val country: String,
    val soldVisibility : Boolean,
    val soldText: String,
    @ColorRes
    val soldTextColor: Int,
    val availableDate: String,
    val soldDate: String,
    val coverPhoto: String,
    val labelPhoto: String
)