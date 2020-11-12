package com.openclassrooms.realestatemanager.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.Property

@Dao
interface PropertyDao {

    @Insert
    suspend fun insertProperty(property: Property)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProperty(property: Property)

    @Query("SELECT * FROM properties ORDER BY property_id DESC")
    fun getAllProperties(): LiveData<List<Property>>
}