package com.openclassrooms.realestatemanager.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.getOrAwaitValue
import com.openclassrooms.realestatemanager.launchFragmentInHiltContainer
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.ui.fragments.RegisterFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class AgentDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: RealEstateDatabase
    private lateinit var agentDao: AgentDao

    @Before
    fun setup() {
        hiltRule.inject()
        agentDao = database.getAgentDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTestAgent() {
        launchFragmentInHiltContainer<RegisterFragment> {

        }
    }

    @Test
    fun insertAgent() = runBlockingTest {
        val agent = Agent("firstName", "lastName", "username", "password")

        agentDao.insertAgent(agent)

        val allAgents = agentDao.getAllAgents().getOrAwaitValue()

        assertThat(allAgents).contains(agent)
    }

    @Test
    fun getAllAgents() = runBlockingTest {
        val agent1 = Agent("firstName1", "lastName1", "username1", "password1")
        val agent2 = Agent("firstName2", "lastName2", "username2", "password2")
        val agent3 = Agent("firstName3", "lastName3", "username3", "password3")

        agentDao.insertAgent(agent1)
        agentDao.insertAgent(agent2)
        agentDao.insertAgent(agent3)

        val allAgents = agentDao.getAllAgents().getOrAwaitValue()

        assertThat(allAgents.size).isEqualTo(3)
    }

}