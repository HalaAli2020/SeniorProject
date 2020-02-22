package com.example.seniorproject.data.models

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.experimental.ExperimentalTypeInference


//fixes implementation changes surrounding kotlin coroutines recent update on flowViaChannel method.
// reverts kotlin coroutines method to back when
//it was working properly. Kotlin coroutines are fairly new; they are still in the experimental phase
    @ExperimentalCoroutinesApi
@UseExperimental(ExperimentalTypeInference::class)
fun <T> flowViaChannel(
    bufferSize: Int = 16,
    @BuilderInference block: CoroutineScope.(channel: SendChannel<T>) -> Unit
): Flow<T> {
    return flow {
        coroutineScope {
            val channel = Channel<T>(bufferSize)
            launch {
                block(channel)
            }

            channel.consumeEach { value ->
                emit(value)
            }
        }
    }
}