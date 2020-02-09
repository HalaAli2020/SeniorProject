package com.example.seniorproject.Dagger

import android.app.Application
import com.example.seniorproject.Dagger.DaggerAppComponent

class SeniorProjApp : Application(){

    lateinit var app_component: AppComponent

    override fun onCreate() {
        super.onCreate()
        app_component = initDagger(this)
    }

    private fun initDagger(app: SeniorProjApp): AppComponent =
        DaggerAppComponent.builder()
        .appModule(AppModule(app))
        .build()
    //unsure of what this does
}






