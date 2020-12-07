package com.openclassrooms.realestatemanager.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.getOrAwaitValue
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyPhoto
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
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
class PropertyDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: RealEstateDatabase
    private lateinit var propertyDao: PropertyDao
    private lateinit var agentDao: AgentDao
    private lateinit var propertyPhotoDao: PropertyPhotoDao
    private lateinit var poiList: ArrayList<Boolean>
    private lateinit var photoList: List<PropertyPhoto>
    private lateinit var agent: Agent

    @Before
    fun setup() {
        hiltRule.inject()
        propertyDao = database.getPropertyDao()
        agentDao = database.getAgentDao()
        propertyPhotoDao = database.getPropertyPhotoDao()

        poiList = ArrayList()
        photoList = listOf()
        agent = Agent("firstName", "lastName", "username", "password")

    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertProperty() = runBlockingTest {
        val property = Property(1, "villa", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "today", "", 1, "coverPhoto", "label")

        agentDao.insertAgent(agent)
        propertyDao.insertProperty(property)

        val allProperties = propertyDao.getAllProperties().getOrAwaitValue()

        assertThat(allProperties).contains(PropertyWithAllData(property, photoList, agent))
    }

    @Test
    fun updateProperty() = runBlockingTest {
        var property = Property(1, "villa", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "today", "", 1, "coverPhoto", "label")

        agentDao.insertAgent(agent)
        propertyDao.insertProperty(property)

        property = Property(1, "manor", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "today", "", 1, "coverPhoto", "label")

        propertyDao.updateProperty(property)

        val allProperties = propertyDao.getAllProperties().getOrAwaitValue()

        assertThat(allProperties).contains(PropertyWithAllData(property, photoList, agent))
    }

    @Test
    fun getAllProperties() = runBlockingTest {
        val property1 = Property(1, "villa", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "today", "", 1, "coverPhoto", "label")
        val property2 = Property(2, "villa", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "today", "", 1, "coverPhoto", "label")
        val property3 = Property(3, "villa", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "today", "", 1, "coverPhoto", "label")

        agentDao.insertAgent(agent)
        propertyDao.insertProperty(property1)
        propertyDao.insertProperty(property2)
        propertyDao.insertProperty(property3)

        val allProperties = propertyDao.getAllProperties().getOrAwaitValue()

        assertThat(allProperties.size).isEqualTo(3)
    }

    @Test
    fun getAllFilteredProperties() = runBlockingTest {
        val property1 = Property(1, "villa", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "31/12/2019", "", 1, "coverPhoto", "label")
        val property2 = Property(2, "villa", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "31/12/2019", "", 1, "coverPhoto", "label")
        val property3 = Property(3, "manor", 1000000, 200, 5, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "11/11/2020", "", 1, "coverPhoto", "label")

        agentDao.insertAgent(agent)
        propertyDao.insertProperty(property1)
        propertyDao.insertProperty(property2)
        propertyDao.insertProperty(property3)

        val allFilteredProperties = propertyDao.getAllFilteredProperties(
                "%", "villa", "0", "100000000", "0",
                "1000000", "3", "5", "0", "1000000", "0", "1000000",
                "01/01/1900", "31/12/2100", "", "", "%", "%",
        ).getOrAwaitValue()

        assertThat(allFilteredProperties.size).isEqualTo(2)
    }

    @Test
    fun getPropertyWithCursor() = runBlockingTest {
        val property = Property(1, "villa", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "today", "", 1, "coverPhoto", "label")

        agentDao.insertAgent(agent)
        propertyDao.insertProperty(property)

        val result = propertyDao.getPropertyWithCursor(1)

        assertThat(result.count).isEqualTo(1)
    }
}