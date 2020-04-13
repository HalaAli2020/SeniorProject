package com.example.seniorproject.Dagger

import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Authentication.PasswordResetActivity
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.MainForum.Fragments.*
import com.example.seniorproject.MainForum.MainForum
import com.example.seniorproject.MainForum.NewPost.FragmentNewImagePost
import com.example.seniorproject.MainForum.NewPost.FragmentNewPost
import com.example.seniorproject.MainForum.Posts.*
import com.example.seniorproject.MainForum.UserProfileActivity
import com.example.seniorproject.Messages.ChatLog
import com.example.seniorproject.Messages.FragmentLatestMessages
import com.example.seniorproject.Messages.NewMessage
import com.example.seniorproject.Utils.Multibindingmodule
import com.example.seniorproject.search.SearchActivity
import dagger.Component
import javax.inject.Singleton

//this interface is a singleton component of the application
//the component connects objects to their dependencies
//usually by use of overriden injection methods

@Singleton
@Component(modules = [AppModule::class, Multibindingmodule::class])
interface AppComponent {

    fun inject(activity: RegisterActivity)
    fun inject(activity: LoginActivity)
    fun inject(fragment: FragmentHome)
    fun inject(activity: PasswordResetActivity)
    fun inject(fragment: FragmentNewPost)
    fun inject(fragment: FragmentList)
    fun inject(fragment: MainForum)
    fun inject(activity: ClickedPost)
    fun inject(activity: CommunityPosts)
    fun inject(activity: UserProfileActivity)
    fun inject(fragment: ProfilePostFragment)
    fun inject(fragment: ProfileCommentFragment)
    fun inject(activity: EditProfileActivity)
    fun inject(activity: UpdateComment)
    fun inject(activity: UpdatePost)
    fun inject(fragment: FragmentNewImagePost)
    fun inject(activity: SearchActivity)
    fun inject(fragment: FragmentSubscriptions)
    fun inject(activity: ChatLog)
    fun inject(activity: NewMessage)
    fun inject(fragment: FragmentLatestMessages)

}