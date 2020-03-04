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
import com.example.seniorproject.MainForum.Adapters.ProfileCommentsAdapter

import com.example.seniorproject.R
import com.example.seniorproject.databinding.FragmentProfileCommentBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile_comment.view.*
import kotlinx.android.synthetic.main.fragment_profile_comment.view.refreshView
import javax.inject.Inject


class ProfileCommentFragment : Fragment() {

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

        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        val binding: FragmentProfileCommentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_comment, container, false)
        myViewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_profile_comment, container, false)

        if (FirebaseAuth.getInstance().uid != null) {
            view.profile_comment_recyclerView.adapter =
                ProfileCommentsAdapter(
                    view.context,
                    myViewModel.getUserProfileComments()
                )
        }



        view.refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(view.context, R.color.blue_theme))
        view.refreshView.setColorSchemeColors(ContextCompat.getColor(view.context, R.color.white))

        view.refreshView.setOnRefreshListener {
            view.profile_comment_recyclerView.adapter =
                ProfileCommentsAdapter(view.context, myViewModel.getUserProfileComments())
            view.refreshView.isRefreshing = false
        }


        binding.profileViewModelCom = myViewModel
        binding.lifecycleOwner = this

        myViewModel.getUserProfileComments()

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        view.profile_comment_recyclerView.layoutManager = linearLayoutManager
        view.profile_comment_recyclerView.adapter =
            ProfileCommentsAdapter(
                view.context,
                myViewModel.getUserProfileComments())


        binding.executePendingBindings()
        return view
    }


}
