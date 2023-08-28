package com.deloitte.mostpopular.domain.usecase

import com.deloitte.mostpopular.data.model.News
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.repository.MostPopularRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostPopularViewUseCase @Inject constructor(private val mostPopularRepository: MostPopularRepository) {
    fun execute(period:String):Flow<Resource<List<News>>>{
        return mostPopularRepository.getMostViewByPeriod(period)
    }

}