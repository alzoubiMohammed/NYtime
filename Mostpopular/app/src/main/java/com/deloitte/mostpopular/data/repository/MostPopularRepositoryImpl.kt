package com.deloitte.mostpopular.data.repository

import com.deloitte.mostpopular.data.datasource.MostPopularRemoteDataSource
import com.deloitte.mostpopular.data.dto.NewsDto
import com.deloitte.mostpopular.data.model.News
import com.deloitte.mostpopular.data.network.NetworkBoundResource
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.repository.MostPopularRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MostPopularRepositoryImpl @Inject constructor(private val mostPopularRemoteDataSource: MostPopularRemoteDataSource) :
    MostPopularRepository {
    override fun getMostViewByPeriod(period: String): Flow<Resource<List<News>>> {
        return object : NetworkBoundResource<List<News>, NewsDto>() {
            override suspend fun loadFromLocalDb(): List<News> {
                return mostPopularRemoteDataSource.loadMostViewFromDatabase(period)
            }

            override suspend fun fetchFromNetwork(): NewsDto {
                return mostPopularRemoteDataSource.getMostViewByPeriodFromNetwork(period)
            }

            override suspend fun saveNetworkResult(response: NewsDto) {
                mostPopularRemoteDataSource.saveMostViewNetworkResult(response,period)
            }

            override fun shouldFetch(data: List<News>): Boolean {
                return mostPopularRemoteDataSource.needToRequest(period)
            }

        }.asFlow()
    }

    override fun searchByTitle(title: String): Flow<List<News>> {
      return  flow { emit(mostPopularRemoteDataSource.searchByTitle(title)) }
    }

}