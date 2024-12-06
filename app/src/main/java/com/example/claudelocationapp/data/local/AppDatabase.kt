package com.example.claudelocationapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.claudelocationapp.data.model.LocationPoint

@Database(entities = [LocationPoint::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}