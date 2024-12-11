
package com.bangkit.crowdwisebali.data.local

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(context: Context) {
    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(context)
        favoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<FavoriteEntity>>{
        return favoriteDao.getAllFavorite()
    }

    fun insert(favoriteEntity: FavoriteEntity){
        executorService.execute { favoriteDao.insert(favoriteEntity) }
    }

    fun delete(favoriteEntity: FavoriteEntity){
        executorService.execute { favoriteDao.delete(favoriteEntity) }
    }

    fun getFavoriteById(id: String): LiveData<FavoriteEntity>{
        return favoriteDao.getFavoriteById(id)
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: FavoriteRepository? = null

        fun getInstance(context: Context): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(context)
            }.also { instance = it }
    }
}
