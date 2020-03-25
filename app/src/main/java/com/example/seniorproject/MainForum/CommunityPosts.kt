package com.example.seniorproject.MainForum

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Adapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.InjectorUtils

import com.example.seniorproject.MainForum.Adapters.CustomAdapter
import com.example.seniorproject.MainForum.Adapters.CustomViewHolders
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.viewModels.CommunityPostViewModel
import kotlinx.android.synthetic.main.activity_community_posts.*
import kotlinx.android.synthetic.main.activity_community_posts.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_new_post.*
import javax.inject.Inject

class CommunityPosts : AppCompatActivity() {

    private lateinit var adapter: CustomAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: CommunityPostViewModel
    lateinit var obse: Observer<in MutableList<Post>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_posts)

        val className = intent.getStringExtra("ClassName")

        post_list_community_name_TV.text = className

        val factory = InjectorUtils.provideCommunityPostViewModelFacotry()
        myViewModel = ViewModelProviders.of(this, factory).get(CommunityPostViewModel::class.java)


        classes_post_RV.layoutManager = LinearLayoutManager(this)

        adapter = CustomAdapter(this, myViewModel.returnClassPosts(className!!), 1)
        classes_post_RV.adapter = adapter


        refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.blue_theme))
        refreshView.setColorSchemeColors(ContextCompat.getColor(this, R.color.white))

        refreshView.setOnRefreshListener {
            refreshView.isRefreshing = false
            classes_post_RV.adapter = CustomAdapter(this, myViewModel.returnClassPosts(className!!), 1)
        }



        obse = Observer<MutableList<Post>> {
            Log.d("obser", " blah")

        }

    }
    //myViewModel.listClasses?.observe(this, obse)




    private fun swap(className: String) {
        val view = window.decorView.rootView
        val ada = CustomAdapter(this, myViewModel.returnClassPosts(className), 0)
        //view!!.post_recyclerView.swapAdapter(ada, true)
        view!!.classes_post_RV.swapAdapter(ada, true)
    }
}





