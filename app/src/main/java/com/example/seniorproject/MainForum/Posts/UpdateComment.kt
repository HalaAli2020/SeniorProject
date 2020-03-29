package com.example.seniorproject.MainForum.Posts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.ProfileCommentsAdapter
import com.example.seniorproject.R
import com.example.seniorproject.databinding.UpdateCommentBinding
import com.example.seniorproject.viewModels.ClickedPostViewModel
import javax.inject.Inject

class UpdateComment  : AppCompatActivity() {

    //private lateinit var adaptercomments: ProfileCommentsAdapter


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ClickedPostViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_comment)


        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this, factory).get(ClickedPostViewModel::class.java)
        val binding: UpdateCommentBinding = DataBindingUtil.setContentView(this,R.layout.update_comment)

        val text: String = intent.getStringExtra("text") ?: "no text"
        val usercomkey: String = intent.getStringExtra("ProfileComKey") ?: "no comkey"
        val userid: String = intent.getStringExtra("PosterID") ?: "no id"
        val crn: String = intent.getStringExtra("crn") ?: "no crn"
        val postkey: String= intent.getStringExtra("Postkey") ?: "no postkey"

        myViewModel.ctext = text
        myViewModel.usercomkey=usercomkey
        myViewModel.comuserid= userid
        myViewModel.usercrn = crn
        myViewModel.postukey = postkey

        binding.clickedModel = myViewModel
        binding.lifecycleOwner = this


    }


}