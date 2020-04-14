package com.example.seniorproject.MainForum.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.HomeAdapter
import com.example.seniorproject.R
import com.example.seniorproject.data.interfaces.ListActivitycallback
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.User
import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject


class FragmentHome : Fragment() {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: HomeFragmentViewModel

    private var obse =  Observer<MutableList<Post>> {

        swap()
    }


    companion object {
        var currentUser: User? = null
    }

    //lifecycleScope coroutines body was launched and list of posts was grabbed using a callback.
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Home"
        loginVerification()

        DaggerAppComponent.create().inject(this)

        myViewModel = ViewModelProvider(this, factory).get(HomeFragmentViewModel::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            myViewModel.getSubsP(object : ListActivitycallback {
                override fun onCallback(list: List<Post>) {
                    view?.invalidate()

                }
            })
        }

    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Home"
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val linearLayoutManager = LinearLayoutManager(context)
        //now the two newest posts show up in home fragment of each subscribed forum
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        view.post_recyclerView.layoutManager = linearLayoutManager

        //initalize recyclerview adapter with list
        view.post_recyclerView.adapter = HomeAdapter(view.context, myViewModel.sendPosts(), 0)

        if (FirebaseAuth.getInstance().uid != null)
            view.post_recyclerView.adapter = HomeAdapter(view.context, myViewModel.sendPosts(), 0)

        Log.d("list size", myViewModel.sendPosts().size.toString())


        view.refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(view.context, R.color.blue_theme))
        view.refreshView.setColorSchemeColors(ContextCompat.getColor(view.context, R.color.white))


        view.refreshView.setOnRefreshListener {
            view.refreshView.isRefreshing = false
            view.post_recyclerView.adapter = HomeAdapter(view.context, myViewModel.sendPosts(), 0)
        }

        binding.homeFragmentViewModel = myViewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()

        return view

    }

    //swap recyclerview with new items when retrieving list of posts
    private fun swap()
    {
        val ada = HomeAdapter(view!!.context, myViewModel.sendPosts(), 0)
        view!!.post_recyclerView.swapAdapter(ada, true)
    }


    private fun loginVerification() {
        if (FirebaseAuth.getInstance().uid == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        view?.post_recyclerView?.swapAdapter(HomeAdapter(view!!.context, myViewModel.sendPosts(), 0), true)

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        view?.post_recyclerView?.adapter = HomeAdapter(view!!.context, myViewModel.sendPosts(), 0)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }


}




