package com.example.respolhpl.data.sources

import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource,
private val localDataSource: LocalDataSource) :
    Repository {

}