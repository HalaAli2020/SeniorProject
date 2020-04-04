package com.example.seniorproject.Utils

import com.example.seniorproject.data.models.Post
import kotlinx.coroutines.flow.Flow

interface FlowCallback {
    fun onFlow(flowCallback : Flow<Post>)
}