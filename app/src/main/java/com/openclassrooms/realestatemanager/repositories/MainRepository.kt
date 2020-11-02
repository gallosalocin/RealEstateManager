package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.db.AgentDao
import com.openclassrooms.realestatemanager.models.Agent
import javax.inject.Inject

class MainRepository @Inject constructor(
        val agentDao: AgentDao
) {
    suspend fun insertAgent(agent: Agent) = agentDao.insertAgent(agent)

    fun observeAllAgents() = agentDao.getAllAgents()

}