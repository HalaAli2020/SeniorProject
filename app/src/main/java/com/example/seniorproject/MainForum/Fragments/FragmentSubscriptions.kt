package com.example.seniorproject.MainForum.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.SubsriptionAdapter
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.SubscriptionsViewModel
import kotlinx.android.synthetic.main.fragment_subscriptions.view.*
import javax.inject.Inject

class FragmentSubscriptions : Fragment() {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: SubscriptionsViewModel

   private var obse = Observer<MutableList<String>>{
       swap()
   }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Subscriptions"
         factory = InjectorUtils.provideSubscriptionsPostViewModelFactory()
        myViewModel = ViewModelProvider(this, factory).get(SubscriptionsViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = "Subscriptions"
        val view = inflater.inflate(R.layout.fragment_subscriptions, container, false)

        myViewModel.usersSubs?.observe(this, obse)
        view.subs_recyclerView.layoutManager = LinearLayoutManager(context)

        val ada = myViewModel.getUserSub()?.let {
            SubsriptionAdapter(
                view.context,
                it
            )
        }
        view.subs_recyclerView.adapter = ada

        view.refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(view.context, R.color.blue_theme))
        view.refreshView.setColorSchemeColors(ContextCompat.getColor(view.context, R.color.white))

        view.refreshView.setOnRefreshListener {
            view.subs_recyclerView.adapter =
                myViewModel.getUserSub()?.let {
                    SubsriptionAdapter(
                        view.context,
                        it
                    )
                }
            view.refreshView.isRefreshing = false
        }

        return view

    }
    private fun swap()
    {
        val ada = SubsriptionAdapter(view!!.context, myViewModel.retsubs())
        view!!.subs_recyclerView.swapAdapter(ada, true)
    }

    override fun onResume() {
        super.onResume()
        val ada = myViewModel.getUserSub()?.let {
            SubsriptionAdapter(
                view!!.context,
                it
            )
        }
        view!!.subs_recyclerView.adapter = ada
    }


}