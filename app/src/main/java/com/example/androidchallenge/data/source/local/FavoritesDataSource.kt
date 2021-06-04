package com.example.androidchallenge.data.source.local

import com.example.androidchallenge.data.model.Series
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesDataSource @Inject constructor(
    private val favoritesDao: FavoritesDao
) {

    fun getAll(): Flow<List<Series>> = favoritesDao.getAll()

    suspend fun add(address: Series) {
        favoritesDao.insert(address)
    }

    suspend fun delete(sid: Int) {
        favoritesDao.delete(sid)
    }
}