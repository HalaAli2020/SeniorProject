package com.example.seniorproject.Dagger

import android.app.Application

//base application component used to generate dagger app component
class SeniorProjApp : Application(){

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        //call function that initializes the dagger app component
        appComponent = initDagger(this)
    }

    //Dagger App component is built here
    private fun initDagger(app: SeniorProjApp): AppComponent =
        DaggerAppComponent.builder()
        .appModule(AppModule(app))
        .build()
}






