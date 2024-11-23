package com.rempawl.respolhpl.data.store.cache

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiCacheProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : DiskCacheProvider(context, "api_responses")
