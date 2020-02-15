package com.example.seniorproject.MainForum

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.R
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.databinding.ActivityClickedPostBinding
import com.example.seniorproject.databinding.ActivityLoginBinding
import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.viewModels.ClickedPostViewModel
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_clicked_post.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject

class ClickedPost : AppCompatActivity() {

    lateinit var Comments : CommentLive
    private lateinit var adapter: CommentsAdapter
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ClickedPostViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_clicked_post)
        var context = applicationContext

        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this,factory).get(ClickedPostViewModel::class.java)
        val binding: ActivityClickedPostBinding
               = DataBindingUtil.setContentView(this, R.layout.activity_clicked_post)
        binding.clickedViewModel = myViewModel
        click_post_title.text = intent.getStringExtra("Title")
        click_post_text.text = intent.getStringExtra("Text")
        //Comments = myViewModel.getComments(intent.getStringExtra("Key"))
        myViewModel.CommentsList.observe(this, Observer {
             Log.d("data change", " Data has changed")
            adapter = CommentsAdapter(context, Comments)
            this.comment_RecyclerView.adapter = adapter
            this.comment_RecyclerView.layoutManager = LinearLayoutManager(context)
           this.comment_RecyclerView.adapter = adapter
           binding.clickedViewModel = myViewModel
            binding.lifecycleOwner = this
        })


    }
}
