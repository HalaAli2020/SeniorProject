package com.example.seniorproject.Utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.viewModels.*
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

    @Binds
    @IntoMap
    @ViewModelKey(ClickedPostViewModel::class)
    abstract fun bindClickedPostViewModel(viewModel: ClickedPostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileFragmentViewModel(viewModel: ProfileViewModel): ViewModel

}