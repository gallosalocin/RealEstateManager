package com.openclassrooms.realestatemanager.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(
        tableName = "properties",
        foreignKeys = [ForeignKey(entity = Agent::class, parentColumns = ["agents_id"], childColumns = ["agent_id"])])
@Parcelize
data class Property(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "properties_id")
        var id: Int = 0,
        var type: String,
        var priceInDollars: Int,
        var areaInMeters: String = "0",
        var nbrRoom: String = "0",
        var nbrBedroom: String = "0",
        var nbrBathroom: String = "0",
        var description: String = "",
        var street: String,
        var postcode: String,
        var city: String,
        var country: String,
        var poi: ArrayList<Boolean> = ArrayList(),
        var isSold: Boolean = false,
        var availableDate: String,
        var soldDate: String = "",
        @ColumnInfo(name = "agent_id")
        var agentId: Int,
        var coverPhoto: String
) : Parcelable

//@Parcelize
//data class Address(
//        var street: String,
//        var postcode: String,
//        var city: String,
//        var country: String
//) : Parcelable