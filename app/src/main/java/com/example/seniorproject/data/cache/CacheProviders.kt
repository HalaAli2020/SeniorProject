package com.example.seniorproject.data.cache

import com.epam.coroutinecache.annotations.Expirable
import com.epam.coroutinecache.annotations.LifeTime
import com.epam.coroutinecache.annotations.ProviderKey
import com.epam.coroutinecache.annotations.UseIfExpired
import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

interface CacheProviders {
    @ProviderKey("TestKey")
    @LifeTime(value= 1L, unit = TimeUnit.MINUTES)
    @Expirable
    @UseIfExpired
    fun getData(data: Flow<PostRepository>) : Flow<PostRepository>
}