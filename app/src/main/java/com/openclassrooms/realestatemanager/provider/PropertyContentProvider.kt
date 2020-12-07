package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.db.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.Property


class PropertyContentProvider : ContentProvider() {

    companion object {
        const val PROVIDER_NAME: String = "com.openclassrooms.realestatemanager.provider"
        val PROPERTIES_TABLE: String = Property::class.java.simpleName
        val URI_PROPERTY: Uri = Uri.parse("content://$PROVIDER_NAME/$PROPERTIES_TABLE")
    }

    override fun onCreate(): Boolean = true

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {

        context?.let { context ->
            val propertyId = ContentUris.parseId(uri)
            RealEstateDatabase.getInstance(context)?.getPropertyDao()?.let {
                val cursor: Cursor = it.getPropertyWithCursor(propertyId)
                cursor.setNotificationUri(context.contentResolver, uri)
                return cursor
            }
        }
        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String = "vnd.android.cursor.item/$PROVIDER_NAME.$PROPERTIES_TABLE"


    // Not implemented because has read only access
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