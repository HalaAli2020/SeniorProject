package com.example.seniorproject.MainForum

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
//import com.example.seniorproject.InjectorUtils
import com.example.seniorproject.databinding.NewPostFragmentBinding
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.R
import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import kotlinx.android.synthetic.main.new_post_fragment.view.*
import javax.inject.Inject

class NewPostFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: NewPostFragmentViewModel

    companion object {
        fun newInstance() = NewPostFragment()
    }

    //private lateinit var viewModel: NewPostFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        DaggerAppComponent.create().inject(this)
        val binding: NewPostFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.new_post_fragment, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(NewPostFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.new_post_fragment, container, false)

        /*val factory = InjectorUtils.provideNewPostViewModelFactory()
        val binding: NewPostFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.new_post_fragment, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(NewPostFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.new_post_fragment, container, false)*/


        val adapter = ArrayAdapter.createFromResource(view.context, R.array.class_list, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        view.spinner2.adapter = adapter

        binding.newPostViewModel = viewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()

        return binding.root
    }



}
