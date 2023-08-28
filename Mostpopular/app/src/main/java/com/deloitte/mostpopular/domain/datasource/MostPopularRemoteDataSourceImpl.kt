package com.deloitte.mostpopular.domain.datasource

import com.deloitte.mostpopular.data.datasource.MostPopularRemoteDataSource
import com.deloitte.mostpopular.data.dto.NewsDto
import com.deloitte.mostpopular.data.local.NewsDao
import com.deloitte.mostpopular.data.local.NewsEntity
import com.deloitte.mostpopular.data.local.LastRequestInfoDao
import com.deloitte.mostpopular.data.local.LastRequestInfoEntity
import com.deloitte.mostpopular.data.model.News
import com.deloitte.mostpopular.data.network.MostPopularEndPoint
import javax.inject.Inject
class MostPopularRemoteDataSourceImpl @Inject constructor(
    private val mostPopularEndPoint: MostPopularEndPoint,
    private val newsDao: NewsDao,
    private val updateLastRequestInfoDao: LastRequestInfoDao,
) : MostPopularRemoteDataSource {
    override suspend fun getMostViewByPeriodFromNetwork(period: String): NewsDto {
        updateLastRequestInfoDao.updateLasRequestInfo(
            LastRequestInfoEntity(
                id = 1,
                System.currentTimeMillis(), period
            )
        )
        return mostPopularEndPoint.getMostViewByPeriod(period)
    }

    override suspend fun loadMostViewFromDatabase(period: String): List<News> {
        return newsDao.getAllItems(period).map {
            News(
                it.id,
                it.title,
                it.description,
                it.imageUrl,
                it.date
            )

        }
    }

    override fun saveMostViewNetworkResult(mostViewedDto: NewsDto, period: String) {

        val mapResult = mostViewedDto.results.map {
            val image = it.media.lastOrNull()?.mediaMetadata?.lastOrNull()?.url?:""
            NewsEntity(
                it.id,
                it.title,
                it.abstract,
                image,
                it.updated,
                period
            )
        }
        newsDao.insert(mapResult)
    }


    override fun needToRequest(period: String): Boolean {
        val lastRequestInfo = updateLastRequestInfoDao.getLastRequestInfo()
        return lastRequestInfo?.let {
            (System.currentTimeMillis() - (it.lastUpdateTimestamp
                ?: DEFAULT_TIME) > MIN_TIME_DELAY) || period != it.period
        } ?: true

    }

    override fun searchByTitle(title: String): List<News> {
        val lastRequestInfo = updateLastRequestInfoDao.getLastRequestInfo()

        return newsDao.search(title, lastRequestInfo?.period!!).map {
                News(
                    it.id,
                    it.title,
                    it.description,
                    it.imageUrl,
                    it.date
                )

        }
    }


    companion object {
        const val DEFAULT_TIME: Long = 0
        const val MIN_TIME_DELAY = 5000

    }

}