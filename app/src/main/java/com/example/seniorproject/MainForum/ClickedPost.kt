package com.example.seniorproject.MainForum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seniorproject.R
import kotlinx.android.synthetic.main.activity_clicked_post.*

class ClickedPost : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicked_post)

        click_post_title.text = intent.getStringExtra("Title")
        click_post_text.text = intent.getStringExtra("Text")

    }
}
