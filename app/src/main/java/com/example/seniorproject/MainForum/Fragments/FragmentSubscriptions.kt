package com.example.seniorproject.MainForum.Fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.SubsriptionAdapter
import com.example.seniorproject.viewModels.SubscriptionsViewModel
import com.example.seniorproject.R
import kotlinx.android.synthetic.main.fragment_subscriptions.view.*
import javax.inject.Inject

class FragmentSubscriptions : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: SubscriptionsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_subscriptions, container, false)
        val factory = InjectorUtils.provideSubscriptionsPostViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(SubscriptionsViewModel::class.java)

        view.subs_recyclerView.layoutManager = LinearLayoutManager(context)
        view.subs_recyclerView.adapter =
            myViewModel.getUserSub()?.let {
                SubsriptionAdapter(
                    view.context,
                    it
                )
            }


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


}