package com.example.seniorproject.MainForum.NewPost

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
//import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.databinding.FragmentNewPostBinding
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.MainForum
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import kotlinx.android.synthetic.main.fragment_new_post.view.*
import javax.inject.Inject

class FragmentNewPost : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: NewPostFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = "New Text Post"
        DaggerAppComponent.create().inject(this)
        val binding: FragmentNewPostBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(NewPostFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_new_post, container, false)

        viewModel.bool.observe(this, Observer<Boolean> {
            Toast.makeText(context, "Your post has been successfully posted!", Toast.LENGTH_LONG).show()
            val intent = Intent(context, MainForum::class.java)
            startActivity(intent)
        })


        val adapter = ArrayAdapter.createFromResource(view.context, R.array.class_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.spinner2.adapter = adapter

        binding.newPostViewModel = viewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()

        return binding.root
    }



}
