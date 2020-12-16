package com.openclassrooms.realestatemanager.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.getOrAwaitValue
import com.openclassrooms.realestatemanager.models.database.Agent
import com.openclassrooms.realestatemanager.models.database.Property
import com.openclassrooms.realestatemanager.models.database.PropertyPhoto
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
class PropertyPhotoDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: RealEstateDatabase
    private lateinit var propertyPhotoDao: PropertyPhotoDao
    private lateinit var propertyDao: PropertyDao
    private lateinit var agentDao: AgentDao
    private lateinit var poiList: ArrayList<Boolean>
    private lateinit var photoList: List<PropertyPhoto>
    private lateinit var agent: Agent
    private lateinit var property: Property

    @Before
    fun setup() {
        hiltRule.inject()
        propertyDao = database.getPropertyDao()
        agentDao = database.getAgentDao()
        propertyPhotoDao = database.getPropertyPhotoDao()

        poiList = ArrayList()
        photoList = listOf()
        agent = Agent("firstName", "lastName", "username", "password")
        property = Property(1, "villa", 1000000, 200, 4, 2, 2,
                "description", "street 55", "25000", "Paris", "France", poiList, false,
                "today", "", 1, "coverPhoto", "label")

    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertPropertyPhoto() = runBlockingTest {
        val propertyPhoto = PropertyPhoto("filename", "label", 1)

        agentDao.insertAgent(agent)
        propertyDao.insertProperty(property)
        propertyPhotoDao.insertPropertyPhoto(propertyPhoto)

        val allPropertyPhotos = propertyPhotoDao.getAllPropertiesPhotos().getOrAwaitValue()

        assertThat(allPropertyPhotos).contains(propertyPhoto)
    }

    @Test
    fun deletePropertyPhoto() = runBlockingTest {
        val propertyPhoto = PropertyPhoto("filename", "label", 1)

        agentDao.insertAgent(agent)
        propertyDao.insertProperty(property)
        propertyPhotoDao.insertPropertyPhoto(propertyPhoto)

        var allPropertyPhotos = propertyPhotoDao.getAllPropertiesPhotos().getOrAwaitValue()
        propertyPhotoDao.deletePropertyPhoto(allPropertyPhotos[0])
        allPropertyPhotos = propertyPhotoDao.getAllPropertiesPhotos().getOrAwaitValue()

        assertThat(allPropertyPhotos).doesNotContain(propertyPhoto)
    }

    @Test
    fun getAllPropertiesPhotos() = runBlockingTest {
        val propertyPhoto1 = PropertyPhoto("filename", "label", 1)
        val propertyPhoto2 = PropertyPhoto("filename", "label", 1)
        val propertyPhoto3 = PropertyPhoto("filename", "label", 1)

        agentDao.insertAgent(agent)
        propertyDao.insertProperty(property)
        propertyPhotoDao.insertPropertyPhoto(propertyPhoto1)
        propertyPhotoDao.insertPropertyPhoto(propertyPhoto2)
        propertyPhotoDao.insertPropertyPhoto(propertyPhoto3)

        val allPropertyPhotos = propertyPhotoDao.getAllPropertiesPhotos().getOrAwaitValue()

        assertThat(allPropertyPhotos.size).isEqualTo(3)
    }

    @Test
    fun getPropertyPhotos() = runBlockingTest {
        val propertyPhoto1 = PropertyPhoto("filename", "label", 1)
        val propertyPhoto2 = PropertyPhoto("filename", "label", 1)

        agentDao.insertAgent(agent)
        propertyDao.insertProperty(property)
        propertyPhotoDao.insertPropertyPhoto(propertyPhoto1)
        propertyPhotoDao.insertPropertyPhoto(propertyPhoto2)

        val allPropertyPhotos = propertyPhotoDao.getPropertyPhotos(1).getOrAwaitValue()

        assertThat(allPropertyPhotos.size).isEqualTo(2)
    }
}