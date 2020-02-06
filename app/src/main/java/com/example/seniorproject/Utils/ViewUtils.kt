package com.example.seniorproject.Utils

import android.content.Context
import android.content.Intent
import com.example.seniorproject.Login.LoginActivity
import com.example.seniorproject.Login.RegisterActivity
import com.example.seniorproject.MainForum.MainForum

fun Context.startRegisterActivity() =
    Intent(this, RegisterActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
fun Context.startMainForum() =
    Intent(this, MainForum::class.java).also{
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }