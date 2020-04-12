package com.example.seniorproject.Utils

import com.example.seniorproject.data.models.Post
import kotlinx.coroutines.flow.Flow

//interface used to get Flow of posts needed for the application
interface FlowCallback {
    fun onFlow(flowCallback : Flow<Post>)
}