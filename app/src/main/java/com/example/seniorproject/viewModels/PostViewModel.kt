package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import com.example.seniorproject.Utils.AuthenticationListener
import io.reactivex.disposables.CompositeDisposable

class PostViewModel : ViewModel() {

    var title: String? = null
    var username: String? = null
    var text: String? = null

    //auth listener
    var authListener: AuthenticationListener? = null

    private val disposables = CompositeDisposable()

    fun getPostInfo()
    {

    }
}