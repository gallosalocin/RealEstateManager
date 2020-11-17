package com.openclassrooms.realestatemanager.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "properties_photos",
        foreignKeys = [
            ForeignKey(
                    entity = Property::class,
                    parentColumns = ["properties_id"],
                    childColumns = ["property_id"],
                    onDelete = ForeignKey.CASCADE)
        ])
@Parcelize
data class PropertyPhoto(
        var filename: Int? = null,
        var label: String = "",
        @ColumnInfo(name = "property_id")
        var propertyId: Int

) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "properties_photos_id")
    var id: Int = 0
}