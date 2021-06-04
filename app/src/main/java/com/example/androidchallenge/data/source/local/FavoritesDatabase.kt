package com.example.androidchallenge.data.source.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.util.Converters

@Database(entities = [Series::class], version = 2)
@TypeConverters(Converters::class)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}