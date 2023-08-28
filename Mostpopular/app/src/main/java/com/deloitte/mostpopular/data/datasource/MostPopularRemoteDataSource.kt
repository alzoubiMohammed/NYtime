package com.deloitte.mostpopular.data.datasource

import com.deloitte.mostpopular.data.dto.NewsDto
import com.deloitte.mostpopular.data.model.News

interface MostPopularRemoteDataSource {
    suspend fun getMostViewByPeriodFromNetwork(period: String): NewsDto
    suspend fun loadMostViewFromDatabase(period: String): List<News>
    fun saveMostViewNetworkResult(mostViewedDto: NewsDto, period: String)
    fun needToRequest(period: String): Boolean
    fun searchByTitle(title: String): List<News>
}