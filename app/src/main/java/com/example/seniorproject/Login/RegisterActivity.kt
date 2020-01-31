package com.example.seniorproject.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.*
import com.example.seniorproject.MainForum.MainForum
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import com.example.seniorproject.data.User
import com.example.seniorproject.databinding.ActivityLoginBinding
import com.example.seniorproject.databinding.ActivityRegisterBinding
import com.google.api.Authentication
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.factory

class RegisterActivity : AppCompatActivity(), AuthenticationListener{


    override fun onStarted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initializeUI()
        Log.d("REG","entered register activity")

    }



    private fun initializeUI(){

        val factory = InjectorUtils.provideAuthViewModelFactory()

        val binding: ActivityRegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)
        var viewModel: AuthenticationViewModel = ViewModelProviders.of(this, factory).get(AuthenticationViewModel::class.java)

        binding.authViewModel = viewModel

        viewModel.authListener = this

    }


}

        /*register_button.setOnClickListener {
        //registerUser()
        }

        already_have_account_textview.setOnClickListener {
            //redirectToLogin()
        }*/

   /* private fun registerUser(){
        val email = email_signup_editText.text.toString()
        val password = password_signup_editTExt.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("Debug", "Email: " + email)
        Log.d("Debug", "pass: " + password)

        //Firebase Authentication
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
                // else if successful
                Log.d("Debug","NEW USER, uid: ${it.result?.user?.uid}")
                Toast.makeText(this, "Account Creation successful", Toast.LENGTH_SHORT).show()
                saveUserToFirebaseDatabase()
                redirectToLogin()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun redirectToLogin(){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    private fun saveUserToFirebaseDatabase(){
        Log.d("Debug", "entered firebase database function")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = User(username_signup_editext.text.toString(),
            email_signup_editText.text.toString(),password_signup_editTExt.text.toString())

        ref.setValue(user).addOnCompleteListener(this){
                    task ->  if (task.isSuccessful){
                Log.d("Debug", "saving to database worked")
            } else {
                Log.d("Debug", "not saved")
            }}.addOnFailureListener(){
                Log.d("Debug", "Error ${it.message}")
            }
    }*/



