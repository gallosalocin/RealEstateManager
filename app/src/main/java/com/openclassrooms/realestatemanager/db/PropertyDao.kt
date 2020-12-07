package com.openclassrooms.realestatemanager.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyWithAllData

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperty(property: Property)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProperty(property: Property)

    @Transaction
    @Query("SELECT * FROM properties ORDER BY properties_id DESC")
    fun getAllProperties(): LiveData<List<PropertyWithAllData>>

    @Transaction
    @Query("SELECT * FROM properties " +
            "INNER JOIN agents ON agent_id = agents_id WHERE " +
            "lastName LIKE :agent" +
            " AND priceInDollars BETWEEN :priceMin AND :priceMax" +
            " AND type LIKE :type" +
            " AND areaInMeters BETWEEN :areaMin AND :areaMax" +
            " AND nbrRoom BETWEEN :roomMin AND :roomMax" +
            " AND nbrBedroom BETWEEN :bedroomMin AND :bedroomMax" +
            " AND nbrBathroom BETWEEN :bathroomMin AND :bathroomMax" +
            " AND availableDate BETWEEN :entryDateMin AND :entryDateMax" +
            " AND soldDate BETWEEN :soldDateMin AND :soldDateMax" +
            " AND city LIKE :city" +
            " AND country LIKE :country"
    )
    fun getAllFilteredProperties(
            agent: String, type: String,
            priceMin: String, priceMax: String,
            areaMin: String, areaMax: String,
            roomMin: String, roomMax: String,
            bedroomMin: String, bedroomMax: String,
            bathroomMin: String, bathroomMax: String,
            entryDateMin: String, entryDateMax: String,
            soldDateMin: String, soldDateMax: String,
            city: String, country: String
    ): LiveData<List<PropertyWithAllData>>


    @Query("SELECT * FROM properties INNER JOIN agents ON agent_id = agents_id WHERE properties_id LIKE :propertyId")
    fun getPropertyWithCursor(propertyId: Long): Cursor

}