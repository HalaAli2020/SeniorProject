package com.example.seniorproject.Utils

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

//Dagger map key for mapping dependencies
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)

@MapKey

internal annotation class ViewModelKey(val value: KClass<out ViewModel>)