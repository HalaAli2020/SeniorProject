package com.example.seniorproject

import com.example.seniorproject.AuthViewModelFactory
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.Repositories.UserAuthRepo

object InjectorUtils {

    fun provideAuthViewModelFactory(): AuthViewModelFactory{
        val userauthrepo: UserAuthRepo = UserAuthRepo.getInstance(FirebaseData())
        return AuthViewModelFactory(userauthrepo)
    }

}