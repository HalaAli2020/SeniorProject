package com.example.seniorproject.viewModels

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Authentication.PasswordResetActivity
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.data.repositories.UserAuthRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/*Authentication Viewmodel class is binded to the Register and Login activity and XML file
This means that variables and functions in this class can be access from the corresponding XML files and activites*/
class AuthenticationViewModel @Inject constructor(private val repository : UserAuthRepo) : ViewModel(){

    var email: String? = null
    var password: String? = null
    var username: String? = null

    //creates instance of firebase authentication
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    //auth listener
    var authListener: AuthenticationListener? = null

    //returns current user
    val user = repository.currentUser()

    fun RegisterUserEmail(){
        viewModelScope.launch(Dispatchers.Main) {
     //Launchting the coroutine on in the viewmodel scope on the main thread
            if (email.isNullOrEmpty() || password.isNullOrEmpty() || username.isNullOrEmpty()) {
                authListener?.onFailure("Please enter your username, email, and password.")
                //auth listener failure if the email, password, or username is left blank
            }
            else if(password!!.length <6){
                authListener?.onFailure("Password must be at least 6 characters long.")
                //auth listener failure if the email length is less then 6 characters
            }
            else{
                repository.RegisterUserEmail(firebaseAuth, email!!, password!!, username!!, object: EmailCallback{
                    override fun getEmail(string: String) {
                        if(!string.isNullOrEmpty()){
                            Log.d("soup2", string)
                            if(string == "Account created!"){
                                authListener?.onSuccess()
                                //authlistener on success located in the register activity called
                            }
                            authListener?.onFailure(string)
                            //generic failure if none of these cases are valid
                        }
                    }
                })
            }
        }
    }

     fun LoginUserEmail() {
         viewModelScope.launch(Dispatchers.Main) {
             //Launchting the coroutine on in the viewmodel scope on the main thread
             if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                 authListener?.onFailure("Please enter both your email and password.")
                 //auth on failure called if the email or password sections are left blank
             } else if(password!!.length <6){
                 authListener?.onFailure("Password must be at least 6 characters long.")
                 //auth on failure called password is less then 6 characters
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
                                     //on failure case for unverified email
                                 }else{
                                     Log.d("soup2", "check")
                                     authListener?.onSuccess()
                                     //on sucess listener located in Login activity called
                                 }
                             }else{
                                 Log.d("soup2", "fail")
                                 authListener?.onFailure(string)
                                 //any other case is a failure
                             }
                         }
                     }

                 })
                 }
             }
         }

    //reset password function
    fun resetUserPassword(){
        viewModelScope.launch(Dispatchers.Main) {
//launching coroutine in viewmodel scope on the main thread
            if (email.isNullOrEmpty()) {
                authListener?.onFailure("Please enter your email.")
                //failure case for no email case
            }
            else{
                repository.resetUserPassword(firebaseAuth, email!!, object : EmailCallback{
                    override fun getEmail(string: String) {
                        if(!string.isNullOrEmpty()){
                            Log.d("soup2", string)
                            if(string == "Email sent!"){
                                authListener?.onSuccess()
                                //success case calls interface in
                            }
                            authListener?.onFailure(string)
                        }
                    }
                })
            }
        }
    }

    //redirects user to Login Activity
     fun redirectToLogin(view: View){
        Intent(view.context, LoginActivity::class.java).also{
            view.context.startActivity(it)
        }
    }

    //redirects user to Register activity
     fun redirectToRegister(view: View){
        Intent(view.context, RegisterActivity::class.java).also{
            view.context.startActivity(it)
        }
    }

    //redirects Password activity
    fun redirectToPasswordReset(view: View){
        Intent(view.context, PasswordResetActivity::class.java).also{
            view.context.startActivity(it)
        }
    }

}