package com.example.seniorproject.Dagger

import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.repositories.MessagesRepo
import com.example.seniorproject.data.repositories.PostRepository
import com.example.seniorproject.data.repositories.SearchRepo
import com.example.seniorproject.viewModels.Factories.*


// viewmodelfactory provider functions for activities that use recyclerviews
object InjectorUtils {

    fun providePostViewModelFactory(): HomeFragmentViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return HomeFragmentViewModelFactory(postrepo)
    }

    fun provideListViewModelFactory(): ListViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return ListViewModelFactory(postrepo)
    }

    fun provideCommunityPostViewModelFacotry(): CommunityPostViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return CommunityPostViewModelFactory(postrepo)
    }

    fun provideSubscriptionsPostViewModelFactory(): SubscriptionsViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return SubscriptionsViewModelFactory(postrepo)
    }

    fun provideProfileViewModelFactory() : ProfileViewModelFactory {
        val postrepo = PostRepository.getInstance(FirebaseData())
        return ProfileViewModelFactory(postrepo)
    }

    fun provideMessagesFragmentViewModelFactory(): MessagesFragmentViewModelFactory{
        val messagerepo = MessagesRepo.getInstance(FirebaseData())
        return MessagesFragmentViewModelFactory(messagerepo)
    }

    fun provideNewMessageViewModelFactory(): NewMessageViewModelFactory{
        val messagerepo = MessagesRepo.getInstance(FirebaseData())
        return NewMessageViewModelFactory(messagerepo)
    }

    fun provideChatLogViewModelFactory(): ChatLogViewModelFactory{
        val messagerepo = MessagesRepo.getInstance(FirebaseData())
        return ChatLogViewModelFactory(messagerepo)
    }
    fun provideSearchViewModelFactory() : SearchViewModelFactory {
        val searchrepo = SearchRepo.getInstance(FirebaseData())
        return SearchViewModelFactory(searchrepo)
    }

}