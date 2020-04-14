package com.example.seniorproject.MainForum.Fragments

import android.content.Context
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
import kotlinx.coroutines.*
import javax.inject.Inject


class FragmentHome : Fragment() {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    @InternalCoroutinesApi
    lateinit var myViewModel: HomeFragmentViewModel
    lateinit var adapter : HomeAdapter





    companion object {
        var currentUser: User? = null
    }

    //lifecycleScope coroutines body was launched and list of posts was grabbed using a callback.
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Home"
        loginVerification()

        DaggerAppComponent.create().inject(this)

        myViewModel = ViewModelProvider(this, factory).get(HomeFragmentViewModel::class.java)

        //adapter = HomeAdapter(context!!, myViewModel.sendPosts(), 0)
      CoroutineScope(Dispatchers.Main.immediate).launch {
            var job = myViewModel.getSubsP(object : ListActivitycallback {
                override fun onCallback(list: List<Post>) {
                    //view!!.invalidate()
                    Log.d("callback", "in")
                    view?.post_recyclerView?.adapter = HomeAdapter(view?.context!!, list, 0)
                    //view?.post_recyclerView?.scrollToPosition(0)
                    //adapter = HomeAdapter(context!!, list, 0)
                    //view?.post_recyclerView?.adapter = adapter

                }
            })

        }

    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Home"
        //val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val viem = inflater.inflate(R.layout.fragment_home, container, false)

        val linearLayoutManager = LinearLayoutManager(context)
        //now the two newest posts show up in home fragment of each subscribed forum
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        viem.post_recyclerView.layoutManager = linearLayoutManager
        myViewModel.live.observe(this.viewLifecycleOwner, Observer {

            this.view!!.post_recyclerView.swapAdapter(HomeAdapter(this.context!!, myViewModel.sendPosts(), 0), true)
            //this.view!!.post_recyclerView.smoothScrollToPosition(0)
        })
        //adapter = HomeAdapter(context!!, myViewModel.sendPosts(), 0)
        adapter = HomeAdapter(context!!, myViewModel.sendPosts(), 0)
        //adapter = HomeAdapter(context!!, myViewModel.sendPosts(), 0)
        /*myViewModel.getSubsP(object : ListActivitycallback {
            override fun onCallback(list: List<Post>) {
                Log.d("reload", "reload")

                adapter = HomeAdapter(context!!, list, 1)
                view.post_recyclerView.swapAdapter(adapter, true)
                //view.post_recyclerView.smoothScrollToPosition(-1)
                //adapter.reload(list)

                //view.post_recyclerView!!.adapter = adapter



            }
        })*/
        if (FirebaseAuth.getInstance().uid != null)
            viem.post_recyclerView?.adapter = adapter

        Log.d("list size", myViewModel.sendPosts().size.toString())


        viem.refreshView?.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(viem.context, R.color.blue_theme))
        viem.refreshView?.setColorSchemeColors(ContextCompat.getColor(viem.context, R.color.white))


        viem.refreshView?.setOnRefreshListener {
            viem.refreshView?.isRefreshing = false
            viem.post_recyclerView?.adapter = HomeAdapter(context!!, myViewModel.sendPosts(), 0)
        }

        ////binding.homeFragmentViewModel = myViewModel
       // binding.lifecycleOwner = this

       // binding.executePendingBindings()
        //initalize recyclerview adapter with list


        return viem

    }

    //swap recyclerview with new items when retrieving list of posts
    @InternalCoroutinesApi
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


    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }


}




