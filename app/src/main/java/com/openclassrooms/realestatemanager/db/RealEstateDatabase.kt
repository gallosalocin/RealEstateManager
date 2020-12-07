package com.openclassrooms.realestatemanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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


    // Only For Content Provider
    companion object {

        @Volatile
        private var INSTANCE: RealEstateDatabase? = null

        fun getInstance(context: Context): RealEstateDatabase? {
            INSTANCE?.let { return it } ?: synchronized(RealEstateDatabase::class.java) {
                INSTANCE?.let { return it }
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RealEstateDatabase::class.java, "RealEstate.db")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE
        }
    }
}