package com.openclassrooms.realestatemanager.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "properties")
@Parcelize
data class Property(
        var type: String,
        var priceInDollars: Int,
        var areaInMeters: String = "0",
        var nbrRoom: String = "0",
        var nbrBedroom: String = "0",
        var nbrBathroom: String = "0",
//        var photo: List<String>,// = ArrayList(),
        var description: String = "",
        var street: String,
        var postcode: String,
        var city: String,
        var country: String,
//        var poi: ArrayList<Boolean> = ArrayList(),
        var isSold: Boolean = false,
        var availableDate: String,
        var soldDate: String = ""
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}