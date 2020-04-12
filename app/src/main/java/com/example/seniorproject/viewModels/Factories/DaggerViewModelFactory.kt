package com.example.seniorproject.viewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

//this generic dagger factory is used for all view models with the help of the multibinding module. the creators is the map of keys of
//the view model
//this is the only place we suppress a warning just because it was suggested as best practice during research into multibinding and dagger
@Suppress("UNCHECKED_CAST")
@Singleton
class DaggerViewModelFactory @Inject constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>> )
    :ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?:

        creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value

        ?: throw IllegalArgumentException("unknown model class $modelClass")

        return try {

            creator.get() as T

        } catch (e: Exception) {

            throw RuntimeException(e)

        }



    }

}





