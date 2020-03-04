package com.example.seniorproject.MainForum.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.CustomAdapter

import com.example.seniorproject.R
import com.example.seniorproject.databinding.FragmentProfilePostBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile__post.view.*
import kotlinx.android.synthetic.main.fragment_profile__post.view.refreshView
import javax.inject.Inject


class ProfilePostFragment : Fragment() {

    private lateinit var adapter: CustomAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile__post, container, false)
        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        val binding: FragmentProfilePostBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile__post, container, false)
        myViewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_profile__post, container, false)

        if (FirebaseAuth.getInstance().uid != null) {
            view.profile_post_recyclerView.adapter =
                CustomAdapter(
                    view.context,
                    myViewModel.getUserProfilePosts(),1
                )
        }


        view.refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(view.context, R.color.blue_theme))
        view.refreshView.setColorSchemeColors(ContextCompat.getColor(view.context, R.color.white))

        view.refreshView.setOnRefreshListener {
            view.profile_post_recyclerView.adapter =
                CustomAdapter(view.context, myViewModel.getUserProfilePosts(),1)
            view.refreshView.isRefreshing = false
        }


        binding.profViewModel = myViewModel
        binding.lifecycleOwner = this

        myViewModel.getUserProfilePosts()
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        view.profile_post_recyclerView.layoutManager = linearLayoutManager
        view.profile_post_recyclerView.adapter =
            CustomAdapter(
                view.context,
                myViewModel.getUserProfilePosts(),
                1
            )

        binding.executePendingBindings()
        return view
    }


}
