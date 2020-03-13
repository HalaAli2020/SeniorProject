package com.example.seniorproject.MainForum.Posts

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.CommentsAdapter
import com.example.seniorproject.MainForum.Adapters.ProfileCommentsAdapter
import com.example.seniorproject.R
import com.example.seniorproject.databinding.ActivityClickedPostBinding
import com.example.seniorproject.databinding.UpdateCommentBinding
import com.example.seniorproject.databinding.UpdateCommentBindingImpl
import com.example.seniorproject.viewModels.ClickedPostViewModel
import kotlinx.android.synthetic.main.update_comment.*
import javax.inject.Inject

class UpdateComment  : AppCompatActivity() {

    private lateinit var adaptercomments: ProfileCommentsAdapter


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ClickedPostViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_comment)


        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this, factory).get(ClickedPostViewModel::class.java)
        val binding: UpdateCommentBinding = DataBindingUtil.setContentView(this,R.layout.update_comment)

        val text: String = intent.getStringExtra("text")
        val usercomkey: String = intent.getStringExtra("ProfileComKey")
        val userid: String = intent.getStringExtra("PosterID")
        val crn: String = intent.getStringExtra("crn")
        val postkey: String= intent.getStringExtra("Postkey")

        myViewModel.ctext = text
        myViewModel.usercomkey=usercomkey
        myViewModel.comuserid= userid
        myViewModel.usercrn = crn
        myViewModel.postukey = postkey

        binding.clickedModel = myViewModel
        binding.lifecycleOwner = this


    }


}