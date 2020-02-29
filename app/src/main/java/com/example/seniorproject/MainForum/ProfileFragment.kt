package com.example.seniorproject.MainForum

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.databinding.FragmentProfile2Binding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.example.seniorproject.viewModels.ProfileViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile2.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
private const val TAG = "MyLogTag"
class ProfileFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel

    companion object {

    }

    private lateinit var adapter: ProfileAdapter
    private lateinit var postLiveData: PostLiveData



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        myViewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)
        val binding: FragmentProfile2Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile2, container, false)
        val view = inflater.inflate(R.layout.fragment_profile2, container, false)

        myViewModel.getUserProfilePosts()
        postLiveData = myViewModel.getUserProfilePosts()
        Log.d(TAG, postLiveData.value.toString())

        adapter = ProfileAdapter(view.context, postLiveData)
        view.profile_recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        view.profile_recyclerView.layoutManager = linearLayoutManager

        //view.profile_recyclerView.adapter = adapter
        //binding.profileViewModel = myViewModel
        binding.lifecycleOwner = this

        myViewModel.getUserProfilePosts()
        binding.executePendingBindings()

        while (PostLiveData.get().value != null) {

            adapter = ProfileAdapter(view.context, postLiveData)
            view.profile_recyclerView.adapter = adapter

            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true


            view.profile_recyclerView.layoutManager = linearLayoutManager
            view.profile_recyclerView.adapter = adapter
            binding.profileViewModel = myViewModel
            binding.lifecycleOwner = this

            binding.executePendingBindings()
        }



        return view
        //return binding.root
    }


}
