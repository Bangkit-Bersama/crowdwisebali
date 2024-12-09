package com.bangkit.crowdwisebali.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM FavoriteEntity ORDER BY place_id ASC")
    fun getAllFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM FavoriteEntity WHERE place_id = :id")
    fun getFavoriteById(id: String): LiveData<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteEntity: FavoriteEntity)

    @Delete
    fun delete(favoriteEntity: FavoriteEntity)
}