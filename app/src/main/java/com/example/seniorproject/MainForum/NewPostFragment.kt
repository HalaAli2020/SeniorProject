package com.example.seniorproject.MainForum

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.seniorproject.InjectorUtils
import com.example.seniorproject.databinding.NewPostFragmentBinding

import com.example.seniorproject.R
import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.example.seniorproject.viewModels.NewPostFragmentViewModel

class NewPostFragment : Fragment() {

    companion object {
        fun newInstance() = NewPostFragment()
    }

    private lateinit var viewModel: NewPostFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val factory = InjectorUtils.provideNewPostViewModelFactory()
        val binding: NewPostFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.new_post_fragment, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(NewPostFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.new_post_fragment, container, false)

        binding.newPostViewModel = viewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()

        return binding.root
    }



}
