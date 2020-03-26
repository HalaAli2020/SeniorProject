package com.example.seniorproject.MainForum.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.CustomAdapter
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.User
import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject


class FragmentHome : Fragment() {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: HomeFragmentViewModel

    var obse =  Observer<MutableList<Post>> {

        swap()
    }


    companion object {
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Home"
        LoginVerification()

        val factory = InjectorUtils.providePostViewModelFactory()

        myViewModel = ViewModelProviders.of(this, factory).get(HomeFragmentViewModel::class.java)
        //val view = inflater.inflate(R.layout.fragment_home, container, false)
        //postLiveData = myViewModel.getSavedPosts()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /*DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this,factory).get(HomeFragmentViewModel::class.java)
        val binding: FragmentHomeBinding = inflate(inflater, R.layout.fragment_home, container, false)
        val view = inflater.inflate(R.layout.fragment_home, container, false)*/
        myViewModel.posts.observe(this, obse)
        activity?.title = "Home"
        //LoginVerification()
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        view.post_recyclerView.layoutManager = linearLayoutManager



        if (FirebaseAuth.getInstance().uid != null)
            view.post_recyclerView.adapter = CustomAdapter(view.context, myViewModel.getSubscribedPosts(), 0)


        view.refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(view.context, R.color.blue_theme))
        view.refreshView.setColorSchemeColors(ContextCompat.getColor(view.context, R.color.white))


        view.refreshView.setOnRefreshListener {
            view.refreshView.isRefreshing = false
            view.post_recyclerView.adapter = CustomAdapter(view.context, myViewModel.getSubscribedPosts(), 0)
        }

        binding.homeFragmentViewModel = myViewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()



        return view

    }
    fun swap()
    {
        var ada = CustomAdapter(view!!.context, myViewModel.getSubscribedPosts(), 0)
        view!!.post_recyclerView.swapAdapter(ada, true)
    }


    private fun LoginVerification() {
        if (FirebaseAuth.getInstance().uid == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        myViewModel.posts.removeObserver(obse)
    }

    override fun onResume() {
        super.onResume()
        myViewModel.posts.observe(this, obse)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        myViewModel.posts.observe(this, obse)
    }


}




