package com.deloitte.mostpopular.domain.repository

import com.deloitte.mostpopular.data.model.News
import com.deloitte.mostpopular.data.network.Resource
import kotlinx.coroutines.flow.Flow

interface MostPopularRepository {
    fun getMostViewByPeriod(period:String): Flow<Resource<List<News>>>
    fun searchByTitle(title:String): Flow<List<News>>

}