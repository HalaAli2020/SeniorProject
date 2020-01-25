package com.example.seniorproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register);

        register_button.setOnClickListener {
        registerUser()

        }
        already_have_account_textview.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun registerUser(){
        val email = email_signup_editText.text.toString()
        val password = password_signup_editTExt.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("Debug", "Email: " + email)
        Log.d("Debug", "pass: $password")

        //Firebase Authentication
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
                // else if successful
                Log.d("Debug","NEW USER, uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener{
                Log.d("Debug", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

