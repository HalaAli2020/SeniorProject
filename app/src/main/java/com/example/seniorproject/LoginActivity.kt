package com.example.seniorproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dont_have_account_textview.setOnClickListener {
            Log.d("MainActivity", "show register activity")
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            val email = email_Login_editText.text.toString()
            val password = password_login_editText.text.toString()

            Log.d("Login", "trying to long in with email $email" )

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                    task ->  if (task.isSuccessful){
                    Log.d("Debug", "signInWithEmail:success")
                } else {
                    Log.d("Debug", "sign in failed")
                }}.addOnFailureListener(){
                    Log.d("Debug", "Failed to create sign in: ${it.message}")
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }


    }
}
