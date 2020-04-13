package com.example.seniorproject.MainForum

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.seniorproject.R
import kotlinx.android.synthetic.main.activity_settings.*

const val PREFS_NAME = "prefs_theme_file"
const val KEY_THEME = "prefs.app.theme"
const val THEME_LIGHT = 0
const val THEME_DARK = 1
const val THEME_SYSTEM = 2


class Settings : AppCompatActivity() {

    private val sharedPrefs by lazy { getSharedPreferences(PREFS_NAME, MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        this.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        onRadioButtonClick()
        setRadioButton()
        //this is triggered once you change the theme and pick an option
        nextActivity.setOnClickListener {
            val intent = Intent(this, MainForum::class.java)
            startActivity(intent)
        }

    }

    //user has option of light mode, night mode, or to follow the system of whatever mode is currently running on the settings of your
    //mobile device
    fun onRadioButtonClick() {
        themeGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.themeLight -> setAppTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
                R.id.themeDark -> setAppTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
                R.id.themeSystem -> setAppTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, THEME_SYSTEM
                )
            }
        }
    }

    private fun setRadioButton() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            themeSystem.visibility = View.VISIBLE
        else
            themeSystem.visibility = View.GONE

        when (getSavedAppTheme()) {
            THEME_LIGHT -> themeLight.isChecked = true
            THEME_DARK -> themeDark.isChecked = true
            THEME_SYSTEM -> themeSystem.isChecked = true
        }
    }

    //sets to night mode
    private fun setAppTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveAppTheme(prefsMode)
    }

    private fun saveAppTheme(theme: Int) = sharedPrefs.edit().putInt(KEY_THEME, theme).apply()

    private fun getSavedAppTheme() = sharedPrefs.getInt(KEY_THEME, THEME_LIGHT)

    //setting up the back button to navigate to the previous screen
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
