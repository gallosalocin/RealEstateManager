package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agents")
data class Agent (
        var username: String = "",
        var password: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}