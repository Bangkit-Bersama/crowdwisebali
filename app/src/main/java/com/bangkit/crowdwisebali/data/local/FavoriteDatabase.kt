package com.bangkit.crowdwisebali.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bangkit.crowdwisebali.helper.Converters

@Database(entities = [FavoriteEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class FavoriteDatabase : RoomDatabase(){
    abstract fun favoriteDao(): FavoriteDao

    companion object{
        @Volatile
        private var INSTANCE: FavoriteDatabase? = null

        fun getDatabase(context: Context): FavoriteDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDatabase::class.java,
                    "favorite_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}