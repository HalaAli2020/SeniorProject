package com.example.seniorproject.utils

import android.content.Context
import android.content.Intent
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.MainForum.MainForum

fun Context.startRegisterActivity() =
    Intent(this, RegisterActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or (Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or (Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(it)
    }

fun Context.startMainActivity(){
    Intent(this, MainForum::class.java).also{
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or (Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(it)
    }
}