package com.example.seniorproject.Authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.AuthenticationListener
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.R
import com.example.seniorproject.InjectorUtils
import com.example.seniorproject.databinding.ActivityLoginBinding
import com.example.seniorproject.utils.startMainActivity


class LoginActivity : AppCompatActivity(), AuthenticationListener {

    private lateinit var viewModel: AuthenticationViewModel


    // User field to pass the to next fragment
    private lateinit var user: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeUI()

    }

    /* dont_have_account_textview.setOnClickListener {
         val intent = Intent(this, RegisterActivity::class.java)
         startActivity(intent)
     }

     login_button.setOnClickListener {
         val email = email_Login_editText.text.toString()
         val password = password_login_editText.text.toString()
         Log.d("Login", "login with  $email")
         FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
             .addOnCompleteListener(this) { task ->

                 if (task.isSuccessful)
                 {
                     val currentuser = FirebaseAuth.getInstance().currentUser
                     currentuser?.let {
                         val username = currentuser.displayName
                         val email = currentuser.email
                         val uid = currentuser.uid
                        user = User(username, email, uid)
                     }

                     Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                     val intent = Intent(this, MainForum::class.java)
                     intent.flags =
                         Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                     startActivity(intent)
                 }
                 else
                 {
                     Log.d("Debug", "sign in failed")
                 }

             }.addOnFailureListener() {
                 Log.d("Debug", "Error ${it.message}")
                 Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
             }
     }*/


    private fun initializeUI() {

        val factory = InjectorUtils.provideAuthViewModelFactory()

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, factory).get(AuthenticationViewModel::class.java)


        binding.authViewModel = viewModel

        viewModel.authListener = this

    }

    override fun onStarted() {
        Log.d("BIGMAN", "SFSDF")
    }

    override fun onSuccess() {
        Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show()
        startMainActivity()
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }




}
