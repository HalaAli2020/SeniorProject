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
import androidx.fragment.app.FragmentManager
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
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class FragmentHome : Fragment() {


    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @InternalCoroutinesApi
    lateinit var myViewModel: HomeFragmentViewModel
    lateinit var adapter: HomeAdapter
    lateinit var job : Job


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


    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Home"
        val viem = inflater.inflate(R.layout.fragment_home, container, false)
        job = CoroutineScope(Dispatchers.Main.immediate).launch {
            //myViewModel.clearRepoList()
            myViewModel.clearLive()

            myViewModel.getSubsP(object : ListActivitycallback {
                override fun onCallback(list: List<Post>) {

                    Log.d("callback", "in")
                    view?.post_recyclerView?.adapter = HomeAdapter(view?.context!!, list, 0)
                    view?.post_recyclerView?.visibility = View.VISIBLE


                }
            })

        }


        val linearLayoutManager = LinearLayoutManager(context)
        //now the two newest posts show up in home fragment of each subscribed forum
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        viem.post_recyclerView.layoutManager = linearLayoutManager

        adapter = HomeAdapter(context!!, myViewModel.sendPosts(), 0)

        if (FirebaseAuth.getInstance().uid != null)
            viem.post_recyclerView?.adapter = adapter




        viem.refreshView?.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(viem.context, R.color.blue_theme))
        viem.refreshView?.setColorSchemeColors(ContextCompat.getColor(viem.context, R.color.white))




        return viem

    }



    private fun loginVerification() {
        if (FirebaseAuth.getInstance().uid == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onStart() {
        super.onStart()
        view?.refreshView?.isRefreshing = true
        myViewModel.live.observe(this.viewLifecycleOwner, Observer {
            view?.refreshView?.isRefreshing = false
            this.view!!.post_recyclerView.swapAdapter(HomeAdapter(this.context!!, myViewModel.sendPosts(), 0), true)

            view?.post_recyclerView?.visibility = View.VISIBLE
        })



    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onPause() {
        super.onPause()
        view?.post_recyclerView!!.adapter = HomeAdapter(context!!, mutableListOf(), 0)
        myViewModel.clearLive()



    }

    override fun onStop() {
        super.onStop()
        view?.refreshView?.isRefreshing = true

    }


    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()

        view?.refreshView?.isRefreshing = true
        myViewModel.live.observe(this.viewLifecycleOwner, Observer {
            view?.refreshView?.isRefreshing = false
            this.view!!.post_recyclerView.swapAdapter(HomeAdapter(this.context!!, myViewModel.sendPosts(), 0), true)

            view?.post_recyclerView?.visibility = View.VISIBLE
        })
        if(!job.isActive)
        {
            CoroutineScope(Dispatchers.Main.immediate).launch {
                //myViewModel.clearRepoList()
                myViewModel.clearLive()

                myViewModel.getSubsP(object : ListActivitycallback {
                    override fun onCallback(list: List<Post>) {

                        Log.d("callback", "in")
                        view?.post_recyclerView?.adapter = HomeAdapter(view?.context!!, list, 0)
                        view?.post_recyclerView?.visibility = View.VISIBLE


                    }
                })

            }

        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        view?.refreshView?.isRefreshing = true
    }
}



