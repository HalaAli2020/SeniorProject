package com.example.seniorproject.Utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.viewModels.*
import com.example.seniorproject.viewModels.Factories.DaggerViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/*how we access the viewmodel provider in the activity after the dagger app component is generated
at this point in time we understand that viewmodel providers of and the app module
 Dagger functions are deprecated, that is the reason for these dead functions and we will work to restructure
the application before the final demo deadline however at the moment these functions are essential for the
application's function for all activities that are using the same viewmodel factory. we implimented this method
of multibinding to reduce the number of viewmodel factories used in the application */
@Module
internal abstract class Multibindingmodule{
    @Binds
    abstract fun bindviewmodelFactory(factory: DaggerViewModelFactory):ViewModelProvider.Factory

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