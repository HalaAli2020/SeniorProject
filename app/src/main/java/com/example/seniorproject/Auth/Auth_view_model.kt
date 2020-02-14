package com.example.seniorproject.Auth

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Auth.AuthListener
import com.example.seniorproject.data.Firebase.FirebaseData
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Auth_view_model(
    private val repo : FirebaseData
) : ViewModel()
//val email : EditText? = null
//val password : EditText? = null
{
val TAG = "AUTH_VIEW_MODEL"
var authListener: AuthListener? = null
var email : String? = null
var password : String? = null
    var username : String? = null
private val disposables = CompositeDisposable()

val user by lazy {
    val user = repo.CurrentUser()
}

 fun Login()
{

    if (email.isNullOrEmpty() || password.isNullOrEmpty())
    {
        authListener?.onFailure("Email or password is null")
        Log.d(TAG, "null pass or email")
    }
    authListener?.onStarted()

   val disposable =repo.LoginUser(email!!, password!!)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            //sending a success callback
            authListener?.onSuccess()
        }, {
            //sending a failure callback
            authListener?.onFailure(it.message!!)
        })
    disposables.add(disposable)
}
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
    fun Registeruser()
    {
        if (email.isNullOrEmpty() || password.isNullOrEmpty())
        {
            authListener?.onFailure("Email or password is null")
            Log.d(TAG, "null pass or email")
        }
        authListener?.onStarted()

        val disposable =repo.RegisterUser(username!!, email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback
                authListener?.onSuccess()
            }, {
                //sending a failure callback
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

}

