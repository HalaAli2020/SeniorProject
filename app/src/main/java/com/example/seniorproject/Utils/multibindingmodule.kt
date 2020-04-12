package com.example.seniorproject.Utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.viewModels.*
import com.example.seniorproject.viewModels.Factories.DaggerViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/*
Once the dagger app component for the application is set up with the help of file SeniorProjApp, we use this multibinding module to bind
all the view models to the generic DaggerViewModelFactory found in the com.seniorproject.viewModels.Factories package. This powerful tool
assigns each view model a key with the help of annotation class mapkey.kt file in the Utils package and puts those keys into a map. As
a result, the generic dagger view model factory has the responsibility of knowing which view model is being binded to it through the
mapkey. This multibinding module saves us from designing view model factories for every single view model which is unnecessary and not
efficient.
 */

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
    @ViewModelKey(SubscriptionsViewModel::class)
    abstract fun bindSubscriptionsViewModel(viewModel: SubscriptionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindListViewModel(viewModel: ListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommunityPostViewModel::class)
    abstract fun bindCommunityPostViewModel(viewModel: CommunityPostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatLogViewModel::class)
    abstract fun bindChatLogViewModel(viewModel: ChatLogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MessagesFragmentViewModel::class)
    abstract fun bindMessagesFragmentViewModel(viewModel: MessagesFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewMessageViewModel::class)
    abstract fun bindNewMessageViewModel(viewModel: NewMessageViewModel): ViewModel
}