package com.example.seniorproject.Search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.models.PostViewHolder
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.example.seniorproject.viewModels.SearchViewModel

class SearchAdapter(context: Context, ViewModel: SearchViewModel) :
    RecyclerView.Adapter<SearchViewHolder>(), Filterable, ListAdapter {
   lateinit var classlist : MutableList<CRN>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.rv_list, parent, false)
        return SearchViewHolder(cellForRow)
    }
    override fun getItemCount(): Int {
        if (classlist.isNotEmpty())
            return classlist.size
        else
            Log.d("NULL", "Isa null")
        return 0
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        //holder.bind(savedPosts!!.value!![position], mContext)
       // Log.d("In list" , savedPosts!!.value!![position].title)

    }

    override fun getFilter(): Filter {

    }

    override fun isEmpty(): Boolean {
        return false
    }

    
}