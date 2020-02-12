package com.example.seniorproject.Utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.viewModels.DaggerViewModelFactory
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class multibindingmodule{
    @Binds
abstract fun bindviewmodelFactory(factory:DaggerViewModelFactory):ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    abstract fun bindAuthenticationViewModel(viewModel: AuthenticationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel::class)
    abstract fun bindHomeFragmentViewModel(viewModel: HomeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewPostFragmentViewModel::class)
    abstract fun bindNewPostFragmentViewModel(viewModel: NewPostFragmentViewModel): ViewModel


}