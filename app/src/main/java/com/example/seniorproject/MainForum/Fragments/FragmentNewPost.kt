package com.example.seniorproject.MainForum

//import com.example.seniorproject.InjectorUtils


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.Dagger.AppComponent
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Fragments.FragmentHome

import com.example.seniorproject.R
import com.example.seniorproject.databinding.FragmentNewPostBinding
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import kotlinx.android.synthetic.main.fragment_new_post.view.*
import javax.inject.Inject


class FragmentNewPost : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: NewPostFragmentViewModel

    companion object {
        fun newInstance() = FragmentNewPost()
    }

    //private lateinit var viewModel: NewPostFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = "New Post"
        //DaggerAppComponent.create().inject(this)
        val binding: FragmentNewPostBinding = DataBindingUtil.inflate(inflater,com.example.seniorproject.R.layout.fragment_new_post , container, false)
        viewModel = ViewModelProviders.of(this, factory).get(NewPostFragmentViewModel::class.java)
        val view = inflater.inflate(com.example.seniorproject.R.layout.fragment_new_post, container, false)


        /*val factory = InjectorUtils.provideNewPostViewModelFactory()
        val binding: NewPostFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(NewPostFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_new_post, container, false)*/


        val adapter = ArrayAdapter.createFromResource(view.context, com.example.seniorproject.R.array.class_list, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the subscriptions of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        view.spinner2.adapter = adapter

        binding.newPostViewModel = viewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()
        binding.newPostBtn.setOnClickListener {
            if (viewModel.titlePost.isNullOrEmpty() || viewModel.textPost.isNullOrEmpty() || viewModel.classSpinner.isNullOrEmpty()) {
                viewModel.postListener?.onFailure("please enter a title and text!")
                Toast.makeText(
                    context,
                    "Please fill in both Email and Password fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else
            {
                viewModel.savePostToDatabase()
                fragmentManager!!.beginTransaction()
                    .replace(R.id.fragmentContainer, FragmentHome())
                    .addToBackStack(null)
                    .commit()
            }

        }


        return binding.root
    }
    fun NewPost()
    {
        if (viewModel.titlePost.isNullOrEmpty() || viewModel.textPost.isNullOrEmpty() || viewModel.classSpinner.isNullOrEmpty()) {
            viewModel.postListener?.onFailure("please enter a title and text!")
            //Toast.makeText((RegisterActivity()), "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        else
        {
            viewModel.savePostToDatabase()
            fragmentManager!!.beginTransaction()
                .replace((view!!.parent as ViewGroup).id, FragmentHome())
                .addToBackStack(null)
                .commit()

        }
        Log.d("SELECTED VALUE:", viewModel.classSpinner.toString())
    }



}

