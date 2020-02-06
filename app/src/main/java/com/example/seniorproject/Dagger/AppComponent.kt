package com.example.seniorproject.Dagger

import com.example.seniorproject.Login.LoginActivity
import com.example.seniorproject.Login.RegisterActivity
import com.example.seniorproject.Utils.authbindingmodule
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.Repositories.UserAuthRepo
import dagger.Component
import javax.inject.Singleton

//this interface is a singleton component of the application
//the component connects objects to their dependencies
//usually by use of overriden injection methods

@Singleton
@Component(modules = [AppModule::class,authbindingmodule::class])
interface AppComponent {

    fun inject(activity: RegisterActivity)
    fun inject(activity: LoginActivity)

}