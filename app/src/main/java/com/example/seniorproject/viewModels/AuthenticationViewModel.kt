package com.example.seniorproject.viewModels

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Authentication.PasswordResetActivity
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.data.repositories.UserAuthRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyLogTag"
class AuthenticationViewModel @Inject constructor(private val repository : UserAuthRepo) : ViewModel(){

    //email and password for the input
    var email: String? = null
    var password: String? = null
    var username: String? = null
    var profileImageUrl: Uri? = null
    var authlisteneruser : FirebaseAuth.AuthStateListener? = null
    var usercur : FirebaseUser? = null

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

            if (!email.isNullOrEmpty() && !password.isNullOrEmpty() && !username.isNullOrEmpty()) {
                //val currentuser = FirebaseAuth.getInstance().currentUser
                    repository.RegisterUserEmail(firebaseAuth, email!!, password!!, username!!)
                    authListener?.onSuccess()

            }
            else{
                authListener?.onFailure("Please enter your username, email, and password.")
            }
        }
    }

     fun LoginUserEmail() {
         viewModelScope.launch(Dispatchers.Main) {
             if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                 authListener?.onFailure("Please enter both your email and password.")
             } else{
                 repository.LoginUserAccount(firebaseAuth, email!!, password!!)
                 if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                     authListener?.onSuccess()
                 }
             }
         }
     }

    fun resetUserPassword(){
        viewModelScope.launch(Dispatchers.Main) {

            if (!email.isNullOrEmpty()) {
                    repository.resetUserPassword(firebaseAuth, email!!)
                    authListener?.onSuccess()
            }
            else{
                authListener?.onFailure("Please enter your email.")
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

    //disposing the disposables
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        //now the register function is no longer being observed
    }

}