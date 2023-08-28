package com.deloitte.mostpopular.domain.usecase

import com.deloitte.mostpopular.data.model.News
import com.deloitte.mostpopular.domain.repository.MostPopularRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchByTitleUseCase @Inject constructor(private val mostPopularRepository: MostPopularRepository) {

    fun execute(title:String): Flow<List<News>> {
       return mostPopularRepository.searchByTitle(title)
    }
}