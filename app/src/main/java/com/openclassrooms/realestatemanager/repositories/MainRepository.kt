package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.db.AgentDao
import com.openclassrooms.realestatemanager.db.PropertyDao
import com.openclassrooms.realestatemanager.db.PropertyPhotoDao
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyPhoto
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import javax.inject.Inject

class MainRepository @Inject constructor(
        private val agentDao: AgentDao,
        private val propertyDao: PropertyDao,
        private val propertyPhotoDao: PropertyPhotoDao
) {

// Agent

    suspend fun insertAgent(agent: Agent) = agentDao.insertAgent(agent)

    fun observeAllAgents() = agentDao.getAllAgents()


    // Property

    fun getPropertyForId(propertyId: Int) : LiveData<PropertyWithAllData> {
        return propertyDao.getPropertyForId(propertyId)
    }

    fun observeAllFilteredProperties(
            agent: String, type: String,
            priceMin: String, priceMax: String,
            areaMin: String, areaMax: String,
            roomMin: String, roomMax: String,
            bedroomMin: String, bedroomMax: String,
            bathroomMin: String, bathroomMax: String,
            entryDateMin: String, entryDateMax: String,
            soldDateMin: String, soldDateMax: String,
            city: String, country: String
    ) =
            propertyDao.getAllFilteredProperties(
                    agent, type, priceMin, priceMax, areaMin, areaMax, roomMin, roomMax, bedroomMin, bedroomMax, bathroomMin, bathroomMax,
                    entryDateMin, entryDateMax, soldDateMin, soldDateMax, city, country)


    suspend fun insertProperty(property: Property) = propertyDao.insertProperty(property)

    suspend fun updateProperty(property: Property) = propertyDao.updateProperty(property)

    fun observeAllProperties() = propertyDao.getAllProperties()


    // Property Photo

    suspend fun insertPropertyPhoto(propertyPhoto: PropertyPhoto) = propertyPhotoDao.insertPropertyPhoto(propertyPhoto)

    suspend fun updatePropertyPhoto(propertyPhoto: PropertyPhoto) = propertyPhotoDao.updatePropertyPhoto(propertyPhoto)

    suspend fun deletePropertyPhoto(propertyPhoto: PropertyPhoto) = propertyPhotoDao.deletePropertyPhoto(propertyPhoto)

    fun observeAllPropertiesPhotos() = propertyPhotoDao.getAllPropertiesPhotos()

    fun observePropertyPhotos(propertyId: Int) = propertyPhotoDao.getPropertyPhotos(propertyId)


//    override suspend fun insertAgent(agent: Agent) {
//        agentDao.insertAgent(agent)
//    }
//
//    override fun observeAllAgents(): LiveData<List<Agent>> {
//        return agentDao.getAllAgents()
//    }
//
//    override fun observeAllFilteredProperties(
//            agent: String, type: String, priceMin: String, priceMax: String, areaMin: String, areaMax: String, roomMin: String, roomMax: String,
//            bedroomMin: String, bedroomMax: String, bathroomMin: String, bathroomMax: String, entryDateMin: String, entryDateMax: String,
//            soldDateMin: String, soldDateMax: String, city: String, country: String
//    ): LiveData<List<PropertyWithAllData>> {
//        return propertyDao.getAllFilteredProperties(
//                agent, type, priceMin, priceMax, areaMin, areaMax, roomMin, roomMax, bedroomMin, bedroomMax, bathroomMin, bathroomMax,
//                entryDateMin, entryDateMax, soldDateMin, soldDateMax, city, country)
//    }
//
//    override suspend fun insertProperty(property: Property) {
//        propertyDao.insertProperty(property)
//    }
//
//    override suspend fun updateProperty(property: Property) {
//        propertyDao.updateProperty(property)
//    }
//
//    override fun observeAllProperties(): LiveData<List<PropertyWithAllData>> {
//        return propertyDao.getAllProperties()
//    }
//
//    override suspend fun insertPropertyPhoto(propertyPhoto: PropertyPhoto) {
//        propertyPhotoDao.insertPropertyPhoto(propertyPhoto)
//    }
//
//    override suspend fun updatePropertyPhoto(propertyPhoto: PropertyPhoto) {
//        propertyPhotoDao.updatePropertyPhoto(propertyPhoto)
//    }
//
//    override suspend fun deletePropertyPhoto(propertyPhoto: PropertyPhoto) {
//        propertyPhotoDao.deletePropertyPhoto(propertyPhoto)
//    }
//
//    override fun observeAllPropertiesPhotos(): LiveData<List<PropertyPhoto>> {
//        return propertyPhotoDao.getAllPropertiesPhotos()
//    }
//
//    override fun observePropertyPhotos(propertyId: Int): LiveData<List<PropertyPhoto>> {
//        return propertyPhotoDao.getPropertyPhotos(propertyId)
//    }


}