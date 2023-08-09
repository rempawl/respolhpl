package com.example.respolhpl.data.store.cache

import android.content.Context
import com.example.respolhpl.data.store.cache.DiskCacheProvider
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiCacheProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    moshi: Moshi,
) : DiskCacheProvider(context, moshi, "api_responses")
