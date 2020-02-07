package com.example.seniorproject.Utils

import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.viewModels.AuthViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class authbindingmodule{

    @Binds
    abstract fun bindviewmodelFactory(factory:AuthViewModelFactory):ViewModelProvider.Factory
}