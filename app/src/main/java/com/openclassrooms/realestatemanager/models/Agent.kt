package com.openclassrooms.realestatemanager.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "agents")
@Parcelize
data class Agent (
        var firstName: String = "",
        var lastName: String = "",
        var username: String = "",
        var password: String = ""
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "agent_id")
    var id: Int = 0
}