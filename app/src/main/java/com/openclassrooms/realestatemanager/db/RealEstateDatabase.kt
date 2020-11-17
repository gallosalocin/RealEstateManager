package com.openclassrooms.realestatemanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyPhoto

@Database(
        entities = [Agent::class, Property::class, PropertyPhoto::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun getAgentDao() : AgentDao
    abstract fun getPropertyDao() : PropertyDao
    abstract fun getPropertyPhotoDao() : PropertyPhotoDao
}