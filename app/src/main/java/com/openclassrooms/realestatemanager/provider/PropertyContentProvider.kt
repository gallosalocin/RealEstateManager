package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.db.AgentDao
import com.openclassrooms.realestatemanager.db.PropertyDao
import com.openclassrooms.realestatemanager.db.PropertyPhotoDao
import com.openclassrooms.realestatemanager.db.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.Property
import javax.inject.Inject

class PropertyContentProvider @Inject constructor(
        private val propertyDao: PropertyDao
) : ContentProvider () {


    companion object {
        const val PROVIDER_NAME: String = "com.openclassrooms.realestatemanager.provider.PropertyContentProvider"
        val PROPERTIES_TABLE: String = Property::class.java.simpleName
        val URI_STRING: String = "content://$PROVIDER_NAME/$PROPERTIES_TABLE"
        val CONTENT_URI: Uri = Uri.parse(URI_STRING)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {

        context?.let {
            val propertyId = ContentUris.parseId(uri)
            val cursor: Cursor = propertyDao.getPropertyWithCursor(propertyId)
            cursor.setNotificationUri(it.contentResolver, uri)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/$PROVIDER_NAME.$PROPERTIES_TABLE"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        throw IllegalStateException("RealEstateManager database has read only access.")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw IllegalStateException("RealEstateManager database has read only access.")
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw IllegalStateException("RealEstateManager database has read only access.")
    }
}