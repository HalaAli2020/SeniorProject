package com.example.seniorproject.MainForum.Fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.ListView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.viewModels.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*
import javax.inject.Inject
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.lifecycle.*
import com.example.seniorproject.MainForum.Adapters.ListAdapter
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostViewHolder

//import com.google.common.eventbus.Subscribe

class FragmentList : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ListViewModel
    var obse : Observer<in MutableList<String>>? = null
    private var ada : ListAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = "All Classes"
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val factory = InjectorUtils.provideListViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
        ada = ListAdapter(view.context, myViewModel)
        view.list_recyclerView.layoutManager = LinearLayoutManager(context)
        view.list_recyclerView.adapter = ada
        Obse()
       myViewModel.UsersSubs?.observe(this ,obse!!)

        return view

    }
    fun Obse()
    {
        obse = Observer<MutableList<String>> { value ->
            Log.d("swap", "Swap")
            swap()


        }


    }
    fun swap()
    {
       // myViewModel.combineSubs()
        ada = ListAdapter(context!!,  myViewModel)
        view!!.list_recyclerView.swapAdapter(ada, true)
    }


}
