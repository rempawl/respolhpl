package com.example.respolhpl.data.sources

import com.example.respolhpl.data.Result
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {
    override suspend fun getProducts(): Result<*> {
        TODO()
    }

    override suspend fun getProductById(id: Long): Result<*> {
        TODO("Not yet implemented")
    }


}