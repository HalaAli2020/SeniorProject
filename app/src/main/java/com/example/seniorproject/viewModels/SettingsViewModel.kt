package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {
    private var listblockedusers: List<String> = mutableListOf()

}