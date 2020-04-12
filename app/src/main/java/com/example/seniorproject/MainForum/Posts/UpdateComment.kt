package com.example.seniorproject.MainForum.Posts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.R
import com.example.seniorproject.databinding.UpdateCommentBinding
import com.example.seniorproject.viewModels.ClickedPostViewModel
import javax.inject.Inject

class UpdateComment  : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ClickedPostViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_comment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Update Comment"


        //initialize inject of dagger app component and initializes view model with generic factory
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this, factory).get(ClickedPostViewModel::class.java)
        val binding: UpdateCommentBinding = DataBindingUtil.setContentView(this,R.layout.update_comment)

        //gets comment information
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

        //sets data binding variable in xml to this view model
        binding.clickedModel = myViewModel
        binding.lifecycleOwner = this


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}