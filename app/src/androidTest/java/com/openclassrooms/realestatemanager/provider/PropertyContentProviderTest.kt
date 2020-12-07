package com.openclassrooms.realestatemanager.provider

import android.content.ContentResolver
import android.content.ContentUris
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.db.RealEstateDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PropertyContentProviderTest {

    private lateinit var contentResolver: ContentResolver
    private lateinit var database: RealEstateDatabase
    private val propertyId: Long = 1

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context

        database = Room.inMemoryDatabaseBuilder(context, RealEstateDatabase::class.java)
                .allowMainThreadQueries().build()

        contentResolver = context.contentResolver
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun getPropertyWhenNoPropertyInserted() {
        val cursor = contentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, propertyId),
                null, null, null, null)
        assertThat(cursor?.count).isEqualTo(0)
        cursor?.close()
    }
}