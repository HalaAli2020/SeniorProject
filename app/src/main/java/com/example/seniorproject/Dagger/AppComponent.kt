package com.example.seniorproject.Dagger

import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Authentication.PasswordResetActivity
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.MainForum.HomeFragment
import com.example.seniorproject.MainForum.MainForum
import com.example.seniorproject.MainForum.NewPostFragment
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
    fun inject(fragment: HomeFragment)
    fun inject(activity: PasswordResetActivity)
    fun inject(fragment: NewPostFragment)
    fun inject(fragment: MainForum)

}