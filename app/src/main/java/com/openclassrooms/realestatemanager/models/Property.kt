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
        var areaInMeters: Int = 0,
        var nbrRoom: Int = 0,
        var nbrBedroom: Int = 0,
        var nbrBathroom: Int = 0,
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
        var coverPhoto: String,
        var labelPhoto: String
) : Parcelable

//@Parcelize
//data class Address(
//        var street: String,
//        var postcode: String,
//        var city: String,
//        var country: String
//) : Parcelable