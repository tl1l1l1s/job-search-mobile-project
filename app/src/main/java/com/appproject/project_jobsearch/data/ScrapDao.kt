package com.appproject.project_jobsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScrapDao {
    @Insert
    fun insertScrap(scrap: ScrapDto)

    @Query("DELETE FROM scrap_table WHERE employmentId=:employmentId")
    fun deleteScrap(employmentId : String)

    @Query("SELECT * from scrap_table WHERE employmentId=:employmentId")
    fun findScrap(employmentId : String) : ScrapDto?

    @Query("SELECT * FROM scrap_table")
    fun getAllScrap() : List<ScrapDto>

}