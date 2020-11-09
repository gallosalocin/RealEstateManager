package com.openclassrooms.realestatemanager.models

import com.openclassrooms.realestatemanager.R

data class Property (
        var type: String,
        var priceInDollars: Int,
        var areaInMeters: String = "0",
        var nbrRoom: String = "0",
        var nbrBedroom: String = "0",
        var nbrBathroom: String = "0",
        var photo: List<String> = listOf(R.drawable.ic_error.toString()),
        var description: String = "",
        var street: String,
        var postcode: String,
        var city: String,
        var country: String,
        var poi: List<Boolean> = listOf(),
        var isSold: Boolean = false,
        var entryDate: String,
        var soldDate: String = "",

        ) {
}