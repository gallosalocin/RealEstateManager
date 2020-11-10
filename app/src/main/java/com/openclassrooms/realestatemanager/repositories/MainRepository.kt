package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.db.AgentDao
import com.openclassrooms.realestatemanager.db.PropertyDao
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Property
import javax.inject.Inject

class MainRepository @Inject constructor(
        val agentDao: AgentDao,
        val propertyDao: PropertyDao
) {
    suspend fun insertAgent(agent: Agent) = agentDao.insertAgent(agent)

    fun observeAllAgents() = agentDao.getAllAgents()


    suspend fun insertProperty(property: Property) = propertyDao.insertProperty(property)

    suspend fun updateProperty(property: Property) = propertyDao.updateProperty(property)

    fun observeAllProperties() = propertyDao.getAllProperties()

}