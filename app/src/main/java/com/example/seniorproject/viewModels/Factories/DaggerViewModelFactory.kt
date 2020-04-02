package com.example.seniorproject.viewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

//this factory provides view models for every activity that does not user a recyclerview
@Suppress("UNCHECKED_CAST")
@Singleton
class DaggerViewModelFactory @Inject constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>> )
    :ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?:

        creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value

        ?: throw IllegalArgumentException("unknown model class " + modelClass)

        return try {

            creator.get() as T

        } catch (e: Exception) {

            throw RuntimeException(e)

        }



    }

}





