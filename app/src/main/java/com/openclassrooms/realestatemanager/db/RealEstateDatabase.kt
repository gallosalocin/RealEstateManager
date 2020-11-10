package com.openclassrooms.realestatemanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Property

@Database(
        entities = [Agent::class, Property::class],
        version = 1,
        exportSchema = false
)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun getAgentDao() : AgentDao
    abstract fun getPropertyDao() : PropertyDao
}