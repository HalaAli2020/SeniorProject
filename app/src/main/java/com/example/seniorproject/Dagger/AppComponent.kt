package com.example.seniorproject.Dagger

import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Authentication.PasswordResetActivity
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.MainForum.*
import com.example.seniorproject.MainForum.Fragments.FragmentHome
import com.example.seniorproject.MainForum.Fragments.FragmentNewPost
import com.example.seniorproject.MainForum.Posts.ClickedPost
import com.example.seniorproject.Utils.multibindingmodule
import dagger.Component
import javax.inject.Singleton

//this interface is a singleton component of the application
//the component connects objects to their dependencies
//usually by use of overriden injection methods

@Singleton
@Component(modules = [AppModule::class,multibindingmodule::class])
interface AppComponent {

    fun inject(activity: RegisterActivity)
    fun inject(activity: LoginActivity)
    fun inject(fragment: FragmentHome)
    fun inject(activity: PasswordResetActivity)
    fun inject(fragment: FragmentNewPost)
    fun inject(fragment: MainForum)
    fun inject(activity: ClickedPost)
    fun inject(fragment: UserProfileActivity)

}