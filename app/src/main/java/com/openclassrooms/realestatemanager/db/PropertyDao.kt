package com.openclassrooms.realestatemanager.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import java.sql.RowId

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperty(property: Property)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProperty(property: Property)

    @Query("SELECT * FROM properties ORDER BY properties_id DESC LIMIT 1")
    fun getLastPropertyAdded(): LiveData<Property>

    @Transaction
    @Query("SELECT * FROM properties ORDER BY properties_id DESC")
    fun getAllProperties(): LiveData<List<PropertyWithAllData>>

}