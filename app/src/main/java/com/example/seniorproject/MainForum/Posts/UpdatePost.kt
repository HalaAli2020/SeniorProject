package com.example.seniorproject.MainForum.Posts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.R
import com.example.seniorproject.databinding.UpdatePostBinding
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import javax.inject.Inject

class UpdatePost : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: NewPostFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_post)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Update Post"


        //initialize inject of dagger app component and initializes view model with generic factory
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this, factory).get(NewPostFragmentViewModel::class.java)
        val binding: UpdatePostBinding = DataBindingUtil.setContentView(this,R.layout.update_post)

        //gets post information
        val text: String = intent.getStringExtra("text") ?: "no text"
        val title: String = intent.getStringExtra("title") ?: "no title"
        val postkey: String = intent.getStringExtra("Classkey") ?: "class key"
        val userid: String = intent.getStringExtra("UserID") ?: "no User id"
        val crn: String = intent.getStringExtra("crn") ?: "no crn"

        myViewModel.ctext = text
        myViewModel.ctitle=title
        myViewModel.userID= userid
        myViewModel.crn = crn
        myViewModel.postKey = postkey

        //sets data binding variable in xml to this view model
        binding.newPostModel = myViewModel
        binding.lifecycleOwner = this

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}