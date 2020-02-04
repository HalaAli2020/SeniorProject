package com.example.seniorproject

import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.repositories.PostRepository
import com.example.seniorproject.data.repositories.UserAuthRepo
import com.example.seniorproject.viewModels.AuthViewModelFactory
import com.example.seniorproject.viewModels.HomeFragmentViewModelFactory

object InjectorUtils {

    fun provideAuthViewModelFactory(): AuthViewModelFactory {
        val userauthrepo: UserAuthRepo = UserAuthRepo.getInstance(FirebaseData())
        return AuthViewModelFactory(userauthrepo)
    }

    fun providePostViewModelFactory(): HomeFragmentViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return HomeFragmentViewModelFactory(postrepo)
    }

}