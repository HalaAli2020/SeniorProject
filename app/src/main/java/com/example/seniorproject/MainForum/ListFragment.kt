package com.example.seniorproject.MainForum


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.InjectorUtils

import com.example.seniorproject.R
import com.example.seniorproject.viewModels.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)


        val factory = InjectorUtils.provideListViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(ListViewModel::class.java)

        view.list_recyclerView.layoutManager = LinearLayoutManager(context)

        Handler().postDelayed({
            view.list_recyclerView.adapter = ListAdapter(myViewModel.getClasses())
        }, 2000)

        return view
    }


}
