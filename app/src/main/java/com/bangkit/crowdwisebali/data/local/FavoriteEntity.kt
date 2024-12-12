package com.bangkit.crowdwisebali.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity
@Parcelize
class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "place_id")
    val id: String = "",

    @ColumnInfo(name = "place_name")
    val name: String? = "",

    @ColumnInfo(name = "formatted_address")
    val location: String? = null,

    @ColumnInfo(name = "rating")
    val rating: @RawValue Any? = null,

    @ColumnInfo(name = "user_rating_count")
    val userRatingCount: Int? = null,

    @ColumnInfo(name = "photos")
    val photosItem: List<String>? = null

): Parcelable