package com.openclassrooms.realestatemanager.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.database.PropertyPhoto

@Dao
interface PropertyPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyPhoto(propertyPhoto: PropertyPhoto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePropertyPhoto(propertyPhoto: PropertyPhoto)

    @Delete
    suspend fun deletePropertyPhoto(propertyPhoto: PropertyPhoto)

    @Query("SELECT * FROM properties_photos")
    fun getAllPropertiesPhotos(): LiveData<List<PropertyPhoto>>

    @Query("SELECT * FROM properties_photos WHERE property_id LIKE :propertyId")
    fun getPropertyPhotos(propertyId: Int): LiveData<List<PropertyPhoto>>
}