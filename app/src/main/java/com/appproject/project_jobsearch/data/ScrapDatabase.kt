package com.appproject.project_jobsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScrapDto::class], version = 1)
abstract class ScrapDatabase : RoomDatabase() {
    abstract fun scrapDao() : ScrapDao

    companion object {
        @Volatile
        private var INSTANCE : ScrapDatabase? = null

        fun getDatabase(context: Context) : ScrapDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    ScrapDatabase::class.java, "scrap_db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}