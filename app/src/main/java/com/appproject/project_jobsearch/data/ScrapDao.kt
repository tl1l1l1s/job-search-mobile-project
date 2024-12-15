package com.appproject.project_jobsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScrapDao {
    @Insert
    fun insertScrap(scrap: ScrapDto)

    @Delete
    fun deleteScrap(scrap: ScrapDto)

    @Query("SELECT * FROM scrap_table")
    fun getAllScrap() : List<ScrapDto>
}