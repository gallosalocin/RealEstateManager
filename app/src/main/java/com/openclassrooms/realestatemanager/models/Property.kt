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
        val id: Int = 0,
        val type: String,
        val priceInDollars: Int,
        val areaInMeters: Int = 0,
        val nbrRoom: Int = 0,
        val nbrBedroom: Int = 0,
        val nbrBathroom: Int = 0,
        val description: String = "",
        val street: String,
        val postcode: String,
        val city: String,
        val country: String,
        val poi: ArrayList<Boolean> = ArrayList(),
        val isSold: Boolean = false,
        val availableDate: String,
        val soldDate: String = "",
        @ColumnInfo(name = "agent_id")
        val agentId: Int,
        val coverPhoto: String,
        val labelPhoto: String
) : Parcelable

//@Parcelize
//data class Address(
//        var street: String,
//        var postcode: String,
//        var city: String,
//        var country: String
//) : Parcelable