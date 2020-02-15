package com.example.seniorproject.viewModels

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Authentication.PasswordResetActivity
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.data.repositories.UserAuthRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "MyLogTag"
class AuthenticationViewModel @Inject constructor(private val repository : UserAuthRepo) : ViewModel(){

    //email and password for the input
    var email: String? = null
    var password: String? = null
    var username: String? = null

    //auth listener
    var authListener: AuthenticationListener? = null


    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()


   val user by lazy {
      repository.currentUser()
    }

    fun Register(){
        if (email.isNullOrEmpty() || password.isNullOrEmpty() || username.isNullOrEmpty()) {
            authListener?.onFailure("Please enter your username, email, or password.")
            return
        }
        authListener?.onStarted()
        val disposable = repository.register(username?:"null",email?:"null",password?:"null")
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        //should this really be the main thread?
            .subscribe({
                //success callback
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message?:"failure")
            })
        disposables.add(disposable)

    }

    fun Login(){
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Please enter both your email and/or password.")
            //Toast.makeText((RegisterActivity()), "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        authListener?.onStarted()
        //calling login from repository
        val disposable = repository.login(email!!,password!!)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            //should this really be the main thread?
            .subscribe({
                //success callback
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

     fun redirectToLogin(view: View){
        Intent(view.context, LoginActivity::class.java).also{
            view.context.startActivity(it)
        }
    }

     fun redirectToRegister(view: View){
        Intent(view.context, RegisterActivity::class.java).also{
            view.context.startActivity(it)
        }
    }

    fun redirectToPasswordReset(view: View){
        Intent(view.context, PasswordResetActivity::class.java).also{
            view.context.startActivity(it)
        }
    }

    fun ResetPassword(){
        if (email.isNullOrEmpty()) {
            authListener?.onFailure("Please enter your email.")
            //  Toast.makeText((RegisterActivity()), "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        authListener?.onStarted()
        //calling login from repository
        val disposable = repository.resetUserPassword(email!!)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            //should this really be the main thread?
            .subscribe({
                //success callback
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)

    }

    //disposing the disposables
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        //now the register function is no longer being observed
    }

}