package com.example.seniorproject.Dagger

import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.Repositories.UserAuthRepo
import dagger.Component
import javax.inject.Singleton

//this interface is a singleton component of the application
//the component connects objects to their dependencies
//usually by use of overriden injection methods

@Singleton
@Component(modules = [AppModule::class,FirebaseData::class,UserAuthRepo::class, AuthenticationViewModel::class])
interface AppComponent {

    fun inject(target: UserAuthRepo)
    fun inject(target: AuthenticationViewModel)
    //target is the module that is the center of the injection
    //^^we are specifying that registeractiviy will require injection from AppComponent
    //Tweeter tweeter() <- Ex
    //we expose types out of a compoent is writing abstact method declarations

}