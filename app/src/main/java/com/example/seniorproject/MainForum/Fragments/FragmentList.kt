package com.example.seniorproject.MainForum.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*
import javax.inject.Inject
import androidx.lifecycle.*
import com.example.seniorproject.MainForum.Adapters.ListAdapter

class FragmentList : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = "All Classes"
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val factory = InjectorUtils.provideListViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(ListViewModel::class.java)

        view.list_recyclerView.layoutManager = LinearLayoutManager(context)
        view.list_recyclerView.adapter = ListAdapter(view.context, myViewModel.returnClasses(), myViewModel)

        return view

    }

}
