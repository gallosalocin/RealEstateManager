package com.openclassrooms.realestatemanager.models.database

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropertyWithAllData(
        @Embedded
        val property: Property,
        @Relation(entity = PropertyPhoto::class, entityColumn = "property_id", parentColumn = "properties_id")
        val photos: List<PropertyPhoto>,
        @Relation(entity = Agent::class, entityColumn = "agents_id", parentColumn = "agent_id")
        val agent: Agent
) : Parcelable