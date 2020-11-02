package com.openclassrooms.realestatemanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.models.Agent

@Database(
        entities = [Agent::class],
        version = 1,
)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun getAgentDao() : AgentDao
}