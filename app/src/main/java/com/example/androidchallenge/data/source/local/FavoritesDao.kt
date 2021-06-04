package com.example.androidchallenge.data.source.local

import androidx.room.*
import com.example.androidchallenge.data.model.Series
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoritesDao {
    @Query("select * from series order by name desc ")
    fun getAll(): Flow<List<Series>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: Series)

    @Update
    suspend fun update(series: Series)

    @Query("DELETE FROM series WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM series")
    suspend fun deleteAll()
}
