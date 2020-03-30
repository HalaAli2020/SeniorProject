package com.example.seniorproject.viewModels

import android.content.Intent
import android.net.Uri
import android.view.View
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Authentication.PasswordResetActivity
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.data.repositories.UserAuthRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

//private const val TAG = "MyLogTag"
class AuthenticationViewModel @Inject constructor(private val repository : UserAuthRepo) : ViewModel(){

    //email and password for the input
    var email: String? = null
    var password: String? = null
    var username: String? = null
    //var profileImageUrl: Uri? = null
    //var authlisteneruser : FirebaseAuth.AuthStateListener? = null
   // var usercur : FirebaseUser? = null

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    //auth listener
    var authListener: AuthenticationListener? = null


    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    val user = repository.currentUser()

    fun RegisterUserEmail(){
        viewModelScope.launch(Dispatchers.Main) {

            if (email.isNullOrEmpty() || password.isNullOrEmpty() || username.isNullOrEmpty()) {
                authListener?.onFailure("Please enter your username, email, and password.")
            }
            else if(password!!.length <6){
                authListener?.onFailure("Password must be at least 6 characters long.")
            }
            else{
                repository.RegisterUserEmail(firebaseAuth, email!!, password!!, username!!, object: EmailCallback{
                    override fun getEmail(string: String) {
                        if(!string.isNullOrEmpty()){
                            Log.d("soup2", string)
                            if(string == "Account created!"){
                                authListener?.onSuccess()
                            }
                            authListener?.onFailure(string)
                        }
                    }
                })
            }
        }
    }

     fun LoginUserEmail() {
         viewModelScope.launch(Dispatchers.Main) {
             if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                 authListener?.onFailure("Please enter both your email and password.")
             } else if(password!!.length <6){
                 authListener?.onFailure("Password must be at least 6 characters long.")
             }
             else
             {
                 repository.LoginUserAccount(firebaseAuth, email!!, password!!, object : EmailCallback{
                     override fun getEmail(string: String) {
                         if(!string.isNullOrEmpty()){
                             Log.d("soup2", string)
                             if(string == "Successful login!"){
                                 if(!FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                                     authListener?.onFailure("Please verify your email.")
                                 }else{
                                     Log.d("soup2", "check")
                                     authListener?.onSuccess()
                                 }
                             }else{
                                 Log.d("soup2", "fail")
                                 authListener?.onFailure(string)
                             }
                         }
                     }

                 })
                 }
             }
         }

    fun resetUserPassword(){
        viewModelScope.launch(Dispatchers.Main) {

            if (email.isNullOrEmpty()) {
                authListener?.onFailure("Please enter your email.")
            }
            else{
                repository.resetUserPassword(firebaseAuth, email!!, object : EmailCallback{
                    override fun getEmail(string: String) {
                        if(!string.isNullOrEmpty()){
                            Log.d("soup2", string)
                            if(string == "Email sent!"){
                                authListener?.onSuccess()
                            }
                            authListener?.onFailure(string)
                        }
                    }
                })
            }
        }
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

}