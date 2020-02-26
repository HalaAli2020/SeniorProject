package com.example.seniorproject.MainForum

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.CommunityPostViewModel
import kotlinx.android.synthetic.main.activity_community_posts.*
import javax.inject.Inject

class CommunityPosts : AppCompatActivity() {

    private lateinit var adapter: CustomAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: CommunityPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_posts)

        val className = intent.getStringExtra("ClassName")

        val factory = InjectorUtils.provideCommunityPostViewModelFacotry()
        myViewModel = ViewModelProviders.of(this,factory).get(CommunityPostViewModel::class.java)

        classes_post_RV.layoutManager = LinearLayoutManager(this)

        classes_post_RV.adapter = CustomAdapter(this, myViewModel.returnClassPosts(className!!))

    }


}

