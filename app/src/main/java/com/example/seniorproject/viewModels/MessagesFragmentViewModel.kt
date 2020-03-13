package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.MessagesRepo
import javax.inject.Inject

class MessagesFragmentViewModel @Inject constructor(private val repository: MessagesRepo) :
    ViewModel() {


}