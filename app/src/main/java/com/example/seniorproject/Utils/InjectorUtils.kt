package com.example.seniorproject.Utils

import android.app.Application
import com.example.seniorproject.viewModels.AuthViewModelFactory
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.Repositories.UserAuthRepo

object InjectorUtils {

    fun provideAuthViewModelFactory(): AuthViewModelFactory {
        val userauthrepo: UserAuthRepo = UserAuthRepo(FirebaseData(app = Application()))
        return AuthViewModelFactory(userauthrepo)
    }

}