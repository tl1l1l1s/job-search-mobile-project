package com.appproject.project_jobsearch.data

class ScrapRepository(private val scrapDao : ScrapDao) {
    suspend fun getAllScraped() : List<ScrapDto> {
        return scrapDao.getAllScrap()
    }

    suspend fun isNotScraped(id: String) : Boolean {
        return scrapDao.findScrap(id) == null
    }

    suspend fun addScrap(scrap: ScrapDto) {
        return scrapDao.insertScrap(scrap)
    }

    suspend fun deleteScrap(id: String) {
        return scrapDao.deleteScrap(id)
    }
}