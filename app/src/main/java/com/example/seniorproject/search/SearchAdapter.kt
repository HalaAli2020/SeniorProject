package com.example.seniorproject.search


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.viewModels.SearchViewModel

/* Adapter for to create a searchable recyclerview to let the user search through all of the classes available
* this uses a custom filter to fliter through the classes in the list the replace the list with a new list of what the user is searching for  */
class SearchAdapter(context: Context, ViewModel: SearchViewModel, Clist : MutableLiveData<MutableList<CRN>>) : RecyclerView.Adapter<SearchViewHolder>() {

    //Initialize variables
    private var classlist: MutableLiveData<MutableList<CRN>> = Clist
    val mContext = context
    private val mViewModel = ViewModel

    //Display communities layout in RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.rv_list, parent, false)
        return SearchViewHolder(cellForRow)
    }

    //Get number of items in data
    override fun getItemCount(): Int {
        if (!classlist?.value.isNullOrEmpty())
            return classlist?.value?.size!!
        return 0
    }

    //Bind data to layout
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(classlist.value!![position], mContext, mViewModel)
    }

    //Display the search results
    fun onfilter(query: String?) {
        if (query.isNullOrEmpty()) {
            classlist = mViewModel.sendlistf()
            notifyDataSetChanged()
        } else {
            classlist = mViewModel.sendlistf()
            val clist: MutableList<CRN> = mutableListOf()
            for ((index, x) in classlist.value!!.withIndex()) {
                if (x.name.contains(query, true)) {
                    clist.add(x)
                }
            }
            classlist.value = clist
            notifyDataSetChanged()
        }

    }
}
