package com.openclassrooms.realestatemanager.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
        tableName = "properties",
        foreignKeys = [ForeignKey(entity = Agent::class, parentColumns = ["agent_id"], childColumns = ["agentId"])])
@Parcelize
data class Property(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "property_id")
        var id: Int? = null,
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
        var soldDate: String = "",
        var agentId: Int
) : Parcelable