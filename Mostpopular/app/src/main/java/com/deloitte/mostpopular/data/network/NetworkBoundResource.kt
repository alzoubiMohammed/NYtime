package com.deloitte.mostpopular.data.network

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class NetworkBoundResource<ResultType, RequestType> {
    fun asFlow() = flow<Resource<ResultType>> {

        emit(Resource.Loading)
        val localData = loadFromLocalDb()
        if (shouldFetch(localData)) {
            try {
                val response = fetchFromNetwork()
                saveNetworkResult(response)
                val localData=loadFromLocalDb()
                emit(Resource.Success(localData))
            } catch (e: Exception) {
                emit(Resource.Error(e))
                emit(Resource.Success(localData))
            }
        } else {
            emit(Resource.Success(localData))
        }
    }.flowOn(IO)

    protected abstract suspend fun loadFromLocalDb(): ResultType
    protected abstract fun shouldFetch(data: ResultType): Boolean
    protected abstract suspend fun fetchFromNetwork(): RequestType
    protected abstract suspend fun saveNetworkResult(response: RequestType)
}